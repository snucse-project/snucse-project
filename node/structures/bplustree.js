class Value{
  constructor(username, start, end){
    this.username = username;
    this.start = start;
    this.end = end;
  }
}

class BPlusTree {
  constructor(degree) {
    this.degree = degree;
    this.root = null;
  }

  insert(key, username, start, end) {
    const newValue = new Value(username, start, end);
    if (this.root === null) {
      this.root = new BPlusTreeNode(this.degree, true);
      this.root.keys[0] = key;
      this.root.values[0] = newValue;
      this.root.numKeys = 1;
    } else {
      if (this.root.numKeys === 2 * this.degree - 1) {
        const newRoot = new BPlusTreeNode(this.degree, false);
        newRoot.children[0] = this.root;
        newRoot.splitChild(0, this.root);

        let i = 0;
        if (newRoot.keys[0] < key) {
          i++;
        }
        newRoot.children[i].insertNonFull(key, newValue);

        this.root = newRoot;
      } else {
        this.root.insertNonFull(key, newValue);
      }
    }
  }

  search(key) {
    return this.root.search(key);
  }

  // for test
  toString() {
      let result = '';
      const queue = [this.root];
  
      while (queue.length > 0) {
        const node = queue.shift();
  
        if (node.isLeaf) {
          result += `Leaf node [${node.keys.map(key => key === null ? 'null' : key).join(', ')}]: `;
          result += node.numKeys + '\n';
        } else {
          result += `Internal node [${node.keys.map(key => key === null ? 'null' : key).join(', ')}]: `;
          result += node.numKeys + '\n';
        }
  
        node.children.filter(child => child !== null).forEach(child => {
          result += `- ${child.keys.map(key => key === null ? 'null' : key).join(', ')} : `;
          result += child.numKeys + '\n';
          if (!child.isLeaf) {
            queue.push(child);
          }
        });
  
        result += '\n';
      }
  
      return result;
    }
  
  visualize() {
      console.log(this.toString());
  }
}

class BPlusTreeNode {
  constructor(degree, isLeaf) {
    this.degree = degree;
    this.isLeaf = isLeaf;
    this.keys = new Array(2 * degree - 1).fill(null);
    this.values = new Array(2 * degree - 1).fill(null);
    this.children = new Array(2 * degree).fill(null);
    this.numKeys = 0;
    this.prev = null;
    this.next = null;
  }

  search(key) {
    let i = 0;
    while (i < this.numKeys && key > this.keys[i]) {
      i++;
    }

    if (this.isLeaf) {
      if (this.keys[i] === key) {
        return this.values[i];
      }
      return null;
    }

    return this.children[i].search(key);
  }

  insertNonFull(key, newValue) {
    let i = this.numKeys - 1;

    if (this.isLeaf) {
      while (i >= 0 && this.keys[i] > key) {
        this.keys[i + 1] = this.keys[i];
        this.values[i + 1] = this.values[i];
        i--;
      }
      this.keys[i + 1] = key;
      this.values[i + 1] = newValue;
      this.numKeys++;
    }
    else {
      while (i >= 0 && this.keys[i] > key) {
        i--;
      }

      if (this.children[i + 1].numKeys === 2 * this.degree - 1) {
        this.splitChild(i + 1, this.children[i + 1]);
        if (this.keys[i + 1] < key) {
          i++;
        }
      }

      this.children[i + 1].insertNonFull(key, newValue);
    }
  }

  splitChild(i, y) {
      const z = new BPlusTreeNode(this.degree, y.isLeaf);
      z.numKeys = this.degree - 1;

      for (let j = 0; j < this.degree - 1; j++) {
          z.keys[j] = y.keys[j + this.degree];
          z.values[j] = y.values[j + this.degree];
          y.keys[j + this.degree] = null;
          y.values[j + this.degree] = null;
      }

      if (!y.isLeaf) {
          for (let j = 0; j < this.degree; j++) {
              z.children[j] = y.children[j + this.degree];
              y.children[j + this.degree] = null;
          }
      }

      y.numKeys = this.degree;

      for (let j = this.numKeys; j >= i + 1; j--) {
          this.children[j + 1] = this.children[j];
      }

      this.children[i + 1] = z;

      for (let j = this.numKeys - 1; j >= i; j--) {
          this.keys[j + 1] = this.keys[j];
          this.values[j + 1] = this.values[j];
      }

      this.keys[i] = y.keys[this.degree - 1];
      this.values[i] = y.values[this.degree - 1];

      this.numKeys++;
  }
}

module.exports = { Value, BPlusTree, BPlusTreeNode };