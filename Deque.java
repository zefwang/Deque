import tester.Tester;
// TODO:
// add all necessary comments and tests

abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  void updateNext(ANode<T> target) {
    this.next = target;
  }

  void updatePrevious(ANode<T> target) {
    this.prev = target;
  }

  abstract int addNode();

  abstract ANode<T> findNode(IPred<T> pred);

  void updateNode(ANode<T> desiredNext, ANode<T> desiredPrev) {
    this.next = desiredNext;
    this.prev = desiredPrev;
    desiredNext.prev = this;
    desiredPrev.next = this;
  }

  boolean isSentinel() {
    return false;
  }
}

class Sentinel<T> extends ANode<T> {
  Sentinel() {
    this.next = this;
    this.prev = this;
  }

  int sizeSent() {
    return this.next.addNode();
  }

  int addNode() {
    return 0;
  }

  /*
   * void addAtHead(T insert) {
   * Node<T> inserted = new Node<T>(insert, this.next, this);
   * }
   *
   * void addAtTail(T insert) {
   * Node<T> inserted = new Node<T>(insert, this, this.prev);
   * }
   *
   * Node<T> removeHead() {
   * if (this.next.equals(this)) {
   * throw new RuntimeException("Empty list");
   * }
   * else {
   * Node<T> fromHead = (Node<T>) this.next;
   * this.updateNext(this.next.next);
   * this.next.next.updatePrevious(this);
   * return fromHead;
   * }
   * }
   *
   * Node<T> removeTail() {
   * if (this.prev.equals(this)) {
   * throw new RuntimeException("Empty list");
   * }
   * else {
   * Node<T> fromTail = (Node<T>) this.prev;
   * this.updatePrevious(this.prev.prev);
   * this.prev.prev.updateNext(this);
   * return fromTail;
   * }
   * }
   */

  ANode<T> findPred(IPred<T> pred) {
    return this.next.findNode(pred);
  }

  ANode<T> findNode(IPred<T> pred) {
    return this;
  }

  boolean isSentinel() {
    return true;
  }
}

class Node<T> extends ANode<T> {
  T data;

  Node(T data) {
    this.next = null;
    this.prev = null;
    this.data = data;
  }

  Node(T data, ANode<T> next, ANode<T> prev) {
    if (next == null || prev == null) {
      throw new IllegalArgumentException("The given node cannot be null");
    }
    this.next = next;
    this.prev = prev;
    this.data = data;
    this.prev.updateNext(this);
    this.next.updatePrevious(this);
  }

  int addNode() {
    return 1 + this.next.addNode();
  }

  ANode<T> findNode(IPred<T> pred) {
    if (pred.apply(this.data)) {
      return this;
    }
    else {
      return this.next.findNode(pred);
    }
  }
}

class Deque<T> {
  Sentinel<T> header;

  Deque(Sentinel<T> header) {
    this.header = header;
  }

  Deque() {
    this.header = new Sentinel<T>();
  }

  int size() {
    return this.header.sizeSent();
  }

  void addAtHead(T insert) {
    Node<T> addNode = new Node<T>(insert);
    addNode.updateNode(this.header.next, this.header);
  }

  void addAtTail(T insert) {
    Node<T> addNode = new Node<T>(insert);
    addNode.updateNode(this.header, this.header.prev);
  }

  T removeFromHead() {
    if (this.header.next.isSentinel()) {
      throw new RuntimeException("Empty list");
    }
    Node<T> toRemove = (Node<T>) this.header.next;
    this.header.updateNode(toRemove.next, this.header.prev);
    return toRemove.data;
  }

  T removeFromTail() {
    if (this.header.prev.isSentinel()) {
      throw new RuntimeException("Empty list");
    }
    Node<T> toRemove = (Node<T>) this.header.prev;
    this.header.updateNode(this.header.next, toRemove.prev);
    return toRemove.data;
  }

  ANode<T> find(IPred<T> pred) {
    return this.header.findPred(pred);
  }

  void removeNode(ANode<T> target) {
    if (!target.isSentinel()) {
      target.prev.updateNode(target.next, target.prev.prev);
    }
  }
}

interface IPred<T> {
  boolean apply(T t);
}

class IsSauharda implements IPred<String> {
  public boolean apply(String given) {
    return given.equals("sauharda");
  }
}

class ExamplesDeque {
  Sentinel<String> empty;
  Deque<String> deque1;
  Sentinel<String> alpha;
  Node<String> n1;
  Node<String> n2;
  Node<String> n3;
  Node<String> n4;
  Deque<String> deque2;
  Sentinel<String> bois;
  Node<String> n5;
  Node<String> n6;
  Node<String> n7;
  Node<String> n8;
  Deque<String> deque3;

  void initData() {
    empty = new Sentinel<String>();
    deque1 = new Deque<String>(empty);

    alpha = new Sentinel<String>();
    n1 = new Node<String>("abc", alpha, alpha);
    n2 = new Node<String>("bcd", alpha, n1);
    n3 = new Node<String>("cde", alpha, n2);
    n4 = new Node<String>("def", alpha, n3);
    deque2 = new Deque<String>(alpha);

    bois = new Sentinel<String>();
    n5 = new Node<String>("sauharda", bois, bois);
    n6 = new Node<String>("daniel", bois, n5);
    n7 = new Node<String>("preston", bois, n6);
    n8 = new Node<String>("ethan", bois, n7);
    deque3 = new Deque<String>(bois);
  }

  // remember to test helpers.
  void testSize(Tester t) {
    initData();
    t.checkExpect(this.deque1.size(), 0);
    t.checkExpect(this.deque2.size(), 4);
    t.checkExpect(this.deque3.size(), 4);

    t.checkExpect(this.empty.sizeSent(), 0);
    t.checkExpect(this.bois.sizeSent(), 4);

    t.checkExpect(this.alpha.addNode(), 0);
    t.checkExpect(this.n1.addNode(), 4);
    t.checkExpect(this.n4.addNode(), 1);
  }

  // IDK IF WE NEED MORE TESTS
  void testAddAtHead(Tester t) {
    initData();
    this.deque3.addAtHead("tester");
    t.checkExpect(this.deque3.header.next, new Node<String>("tester", n5, this.deque3.header));
  }

  void testAddAtTail(Tester t) {
    initData();
    this.deque3.addAtTail("testeragain");
    t.checkExpect(this.deque3.header.prev, new Node<String>("testeragain", this.deque3.header, n8));
  }

  void testRemoveFromHead(Tester t) {
    this.initData();

    t.checkException(new RuntimeException("Empty list"), this.deque1, "removeFromHead");
    t.checkExpect(this.deque2.removeFromHead(), "abc");
    t.checkExpect(this.deque2.removeFromHead(), "bcd");
    t.checkExpect(this.deque3.removeFromHead(), "sauharda");
  }

  void testRemoveFromTail(Tester t) {
    this.initData();

    t.checkException(new RuntimeException("Empty list"), this.deque1, "removeFromTail");
    t.checkExpect(this.deque2.removeFromTail(), "def");
    t.checkExpect(this.deque2.removeFromTail(), "cde");
    t.checkExpect(this.deque3.removeFromTail(), "ethan");
  }

  void testFind(Tester t) {
    initData();
    IsSauharda test1 = new IsSauharda();

    t.checkExpect(this.deque3.find(test1), this.n5);
    t.checkExpect(this.deque2.find(test1), this.alpha);
    t.checkExpect(this.deque1.find(test1), this.empty);

    t.checkExpect(this.n5.findNode(test1), this.n5);
    t.checkExpect(this.n8.findNode(test1), this.bois);
    t.checkExpect(this.bois.findNode(test1), this.bois);
  }

  void testApply(Tester t) {
    initData();
    IsSauharda testing = new IsSauharda();

    t.checkExpect(testing.apply("sauharda"), true);
    t.checkExpect(testing.apply("Sauharda"), false);
    t.checkExpect(testing.apply("sau"), false);
  }

  void testRemoveNode(Tester t) {
    initData();
    this.deque3.removeNode(n6);

    t.checkExpect(this.n5.next, this.n7);
    t.checkExpect(this.n7.prev, this.n5);
  }

  void testIsSentinel(Tester t) {
    initData();

    t.checkExpect(this.alpha.isSentinel(), true);
    t.checkExpect(this.n1.isSentinel(), false);
  }
}
