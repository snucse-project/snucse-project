class LRUNode {
  constructor(key, data, size){
    this.key = key;
    this.data = data;
    this.prev = null;
    this.next = null;
    this.size = size;
  }
}

class CacheLRU {
  constructor(limit) {
    this.head = null;
    this.tail = null;
    this.size = 0;
    this.limit = limit;
    this.hashTable = new Map();
  }

  insert(key, data, size) {
    const newNode = new LRUNode(key, data, size);
    
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
    // console.log("Inserted data, cur size: " + this.size);
    this.hashTable.set(key, newNode);
    if (this.size > this.limit){
      this.removeByLRU();
      // console.log("Evicted by LRU, cur size: " + this.size);
    }
  }

  removeByLRU(){
    while(this.size > this.limit){
      this.size = this.size - this.head.size;
      this.hashTable.delete(this.head.key);
      let tmp = this.head;
      this.head = tmp.next;
      tmp.data = null;
      tmp.prev = null;
      tmp.next = null;
      tmp.key = null
    }
  }

  find(key) {
    if(this.hashTable.has(key)){
      // console.log("cache hit!");
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
      // console.log("cache miss...");
      return null;
    }
  }

  has(key) {
    return this.hashTable.has(key);
  }
}

class ClockNode {
  constructor(key, data, size){
    this.key = key;
    this.data = data;
    this.next = null;
    this.size = size;
    this.refBit = true;
  }
}

class CacheClock {
  constructor(limit) {
    this.cur = null;
    this.size = 0;
    this.limit = limit;
    this.hashTable = new Map();
  }

  insert(key, data, size) {
    const newNode = new ClockNode(key, data, size);
    
    if(!this.cur) {
      this.cur = newNode;
      newNode.next = newNode;
    }
    else {
      newNode.next = this.cur.next;
      this.cur.next = newNode;
      this.cur = this.cur.next;
    }
    
    this.size = this.size + newNode.size;
    // console.log("Inserted data, cur size: " + this.size);
    this.hashTable.set(key, newNode);
    if (this.size > this.limit){
      this.removeByClock();
      // console.log("Evicted by Clock, cur size: " + this.size);
    }
  }

  removeByClock(){
    while(this.size > this.limit){
      if(this.cur.next.refBit == false){
        this.size -= this.cur.next.size;
        this.hashTable.delete(this.cur.next.key);
        this.cur.next = this.cur.next.next;
      }
      else {
        this.cur.next.refBit = false;
        this.cur = this.cur.next;
      }
    }
  }

  find(key) {
    if(this.hashTable.has(key)){
      // console.log("cache hit!");
      const findNode = this.hashTable.get(key);
      findNode.refBit = 1;
      return findNode.data;
    }
    else {
      // console.log("cache miss...");
      return null;
    }
  }

  has(key) {
    return this.hashTable.has(key);
  }
}

module.exports = {CacheLRU, CacheClock};