class Node {
  constructor(key, data, size){
    this.key = key;
    this.data = data;
    this.prev = null;
    this.next = null;
    this.size = size;
  }
}

class Cache {
  constructor(limit) {
    this.head = null;
    this.tail = null;
    this.size = 0;
    this.limit = limit;
    this.hashTable = new Map();
  }

  insert(key, data, size) {
    const newNode = new Node(key, data, size);
    
    if(!this.head) {
      this.head = newNode;
      this.tail = newNode;
    }
    else {
      this.tail.next = newNode;
      newNode.prev = this.tail;
      this.tail = newNode;
      
    }
    
    this.size = this.size + newNode.size;
    console.log("Inserted data, cur size: " + this.size);
    this.hashTable.set(key, newNode);
    if (this.size > this.limit){
      this.removeByLRU();
      console.log("Evicted by LRU, cur size: " + this.size);
    }
  }

  removeByLRU(){
    while(this.size > this.limit){
      this.size = this.size - this.head.size;
      this.hashTable.delete(this.head.key);
      this.head = this.head.next;
    }
  }

  find(key) {
    if(this.hashTable.has(key)){
      console.log("cache hit!");
      const findNode = this.hashTable.get(key);
      if(findNode.prev !== null)
        findNode.prev.next = findNode.next;
      if(findNode.next !== null)
        findNode.next.prev = findNode.prev;
      this.tail.next = findNode;
      findNode.prev = this.tail;
      this.tail = findNode;
      return findNode.data;
    }
    else {
      console.log("cache miss...");
      return null;
    }
  }
}

module.exports = {Cache};