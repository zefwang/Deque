import tester.Tester;

// The abstracted class which includes Node and Sentinel
abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  // If a node, adds one. If sentinel, adds 0.
  abstract int addNode();

  // Given a predicate, finds the Node. If not found, returns Sentinel.
  abstract ANode<T> findNode(IPred<T> pred);

  // The abstracted method to add or remove ANode from a Deque.
  void updateNode(ANode<T> desiredNext, ANode<T> desiredPrev) {
    this.next = desiredNext;
    this.prev = desiredPrev;
    desiredNext.prev = this;
    desiredPrev.next = this;
  }

  // Is this ANode a Sentinel?
  boolean isSentinel() {
    // Initialized as false; redefined in Sentinel.
    return false;
  }
}

// Represents the header of a Deque.
class Sentinel<T> extends ANode<T> {
  // Initialized in an empty deque. Thus, the sentinel
  // initially refers to itself in both directions.
  Sentinel() {
    this.next = this;
    this.prev = this;
  }

  // Determines the number of Nodes in a deque—starts at Sentinel.
  int sizeSent() {
    return this.next.addNode();
  }

  // Returns 0 as it is not a Node but rather a Sentinel.
  int addNode() {
    return 0;
  }

  // Asks if the next ANode is the one we want.
  ANode<T> findPred(IPred<T> pred) {
    return this.next.findNode(pred);
  }

  // If the end of the Deque is reached such that the Sentinel
  // is reached, returns the Sentinel.
  ANode<T> findNode(IPred<T> pred) {
    return this;
  }

  // Is This a Sentinel? It is.
  boolean isSentinel() {
    return true;
  }
}

// One of the Nodes on a Deque. Not a Sentinel.
class Node<T> extends ANode<T> {
  T data;

  // Initialized to have no next or previous.
  Node(T data) {
    this.next = null;
    this.prev = null;
    this.data = data;
  }

  // A constructor that accepts the next and previous ANode—cannot be null.
  // Updates the given nodes' relations to add This Node.
  Node(T data, ANode<T> next, ANode<T> prev) {
    if (next == null || prev == null) {
      throw new IllegalArgumentException("The given node cannot be null");
    }
    this.next = next;
    this.prev = prev;
    this.data = data;
    this.updateNode(next, prev);
  }

  // Adds 1 to the count of Nodes and continues through
  // the rest of the Deque because This is a Node.
  int addNode() {
    return 1 + this.next.addNode();
  }

  // If This is the Node we're looking for, returns This.
  // Else, repeats through the rest of the Deque.
  ANode<T> findNode(IPred<T> pred) {
    if (pred.apply(this.data)) {
      return this;
    }
    else {
      return this.next.findNode(pred);
    }
  }
}

// A Deque with a Sentinel and 0 or more Nodes.
class Deque<T> {
  Sentinel<T> header;

  // If given Sentinel, assigns it as the header of This Deque.
  Deque(Sentinel<T> header) {
    this.header = header;
  }

  // If no Sentinel given, creates one and
  // assigns it as the header of This Deque.
  Deque() {
    this.header = new Sentinel<T>();
  }

  // Determines the number of Nodes in This Deque.
  int size() {
    return this.header.sizeSent();
  }

  // EFFECT: Adds a Node with the given data to the head of the Deque.
  void addAtHead(T insert) {
    Node<T> addNode = new Node<T>(insert);
    addNode.updateNode(this.header.next, this.header);
  }

  // EFFECT: Adds a Node with the given data to the tail of the Deque.
  void addAtTail(T insert) {
    Node<T> addNode = new Node<T>(insert);
    addNode.updateNode(this.header, this.header.prev);
  }

  // Returns the data of the removed Node from the head of This Deque.
  // EFFECT: Removes the Node from the head of This Deque.
  T removeFromHead() {
    if (this.header.next.isSentinel()) {
      throw new RuntimeException("Empty list");
    }
    Node<T> toRemove = (Node<T>) this.header.next;
    this.header.updateNode(toRemove.next, this.header.prev);
    return toRemove.data;
  }

  // Returns the data of the removed Node from the tail of This Deque.
  // EFFECT: Removes the Node from the tail of This Deque.
  T removeFromTail() {
    if (this.header.prev.isSentinel()) {
      throw new RuntimeException("Empty list");
    }
    Node<T> toRemove = (Node<T>) this.header.prev;
    this.header.updateNode(this.header.next, toRemove.prev);
    return toRemove.data;
  }

  // Returns the first Node that passes the
  // Pred given or Sentinel if none pass the Pred.
  ANode<T> find(IPred<T> pred) {
    return this.header.findPred(pred);
  }

  // EFFECT: Removes the given Node from This Deque.
  void removeNode(ANode<T> target) {
    if (!target.isSentinel()) {
      target.prev.updateNode(target.next, target.prev.prev);
    }
  }
}

// Represents the predicate to be asked against each ANode in a Deque
interface IPred<T> {
  boolean apply(T t);
}

// Example IPred. Is the data of the node "sauharda"?
class IsSauharda implements IPred<String> {
  public boolean apply(String given) {
    return given.equals("sauharda");
  }
}

// Examples and tests class.
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

  // Initializes the data.
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

  // Tests the size of each Deque, using helpers: sizeSent() & addNode().
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

  void testAddAtHead(Tester t) {
    initData();
    this.deque3.addAtHead("tester");
    t.checkExpect(this.deque3.header.next, new Node<String>("tester", n5, this.deque3.header));
  }

  void testAddAtTail(Tester t) {
    initData();
    this.deque3.addAtTail("testeragain");
    t.checkExpect(this.deque3.header.prev, new Node<String>("testeragain", this.deque3.header, n8));

    initData();
    t.checkExpect(this.deque3.header.prev, this.n8);
    this.deque3.addAtTail("Bye");
    t.checkExpect(((Node<String>) this.deque3.header.prev).data, "Bye");
  }

  void testRemoveFromHead(Tester t) {
    this.initData();

    t.checkException(new RuntimeException("Empty list"), this.deque1, "removeFromHead");
    t.checkExpect(this.deque2.removeFromHead(), "abc");
    t.checkExpect(this.deque2.removeFromHead(), "bcd");
    t.checkExpect(this.deque3.removeFromHead(), "sauharda");

    initData();
    t.checkExpect(this.deque3.header.next, this.n5);
    this.deque3.addAtHead("hello");
    t.checkExpect(((Node<String>) this.deque3.header.next).data, "hello");
  }

  void testRemoveFromTail(Tester t) {
    this.initData();

    t.checkException(new RuntimeException("Empty list"), this.deque1, "removeFromTail");
    t.checkExpect(this.deque2.removeFromTail(), "def");
    t.checkExpect(this.deque2.removeFromTail(), "cde");
    t.checkExpect(this.deque3.removeFromTail(), "ethan");
  }

  // Tests find() and related findNode().
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

  // Tests updateNode().
  void testUpdatingNodeAndDeque(Tester t) {
    initData();
    t.checkExpect(this.n1.next, this.n2);
    t.checkExpect(this.n1.prev, this.alpha);
    this.n1.updateNode(this.n3, this.n4);
    t.checkExpect(this.n1.next, this.n3);
    t.checkExpect(this.n1.prev, this.n4);

    // Removing a node with updateNode()
    t.checkExpect(this.deque2.header.next, this.n1);
    this.deque2.header.updateNode(this.n2, this.n4);
    t.checkExpect(this.deque2.header.next, this.n2);
    t.checkExpect(this.deque2.header.prev, this.n4);

  }

  // Tests the application of the predicate IPred
  void testApply(Tester t) {
    initData();
    IsSauharda testing = new IsSauharda();

    t.checkExpect(testing.apply("sauharda"), true);
    t.checkExpect(testing.apply("Sauharda"), false);
    t.checkExpect(testing.apply("sau"), false);
  }

  // Tests removeNode()
  void testRemoveNode(Tester t) {
    initData();
    this.deque3.removeNode(n6);

    t.checkExpect(this.n5.next, this.n7);
    t.checkExpect(this.n7.prev, this.n5);
  }

  // Tests isSentinel()
  void testIsSentinel(Tester t) {
    initData();

    t.checkExpect(this.alpha.isSentinel(), true);
    t.checkExpect(this.n1.isSentinel(), false);
  }

  void testFindPred(Tester t) {
    initData();
    IsSauharda testing = new IsSauharda();
    t.checkExpect(this.bois.findPred(testing), this.n5);
    t.checkExpect(this.alpha.findPred(testing), this.alpha);
  }
}
