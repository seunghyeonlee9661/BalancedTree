# 균형 트리 (Balanced Tree)

## 1. 균형 트리란?
![image](https://github.com/user-attachments/assets/6725d13e-8e98-4909-bee5-a695f7fd9246)

균형 트리(Balanced Tree)는 트리 구조의 일종으로, 각 노드의 자식 트리들의 높이 차이가 일정 범위 내에 있는 트리입니다. 즉, 트리의 균형을 유지하여 탐색, 삽입, 삭제 등의 연산이 최적의 시간 복잡도에서 수행될 수 있도록 보장합니다.
균형 트리의 중요성은 **효율적인 데이터 접근과 연산**에 있습니다. 비균형 트리는 특정 연산에서 시간 복잡도가 O(n)에 달할 수 있지만, 균형 트리는 최악의 경우에도 O(log n)의 시간 복잡도를 보장합니다. 이는 특히 **대규모 데이터 처리**에서 성능을 크게 향상시킬 수 있습니다. 데이터베이스의 인덱스가 가장 좋은 예시입니다.

## 2. 데이터베이스의 인덱스
![image](https://github.com/user-attachments/assets/0dd95df1-4ac8-4cbb-b4e5-bfb5c5b88e67)
균형 트리는 데이터베이스에서 인덱스 구조로 자주 사용됩니다. 데이터베이스에서 효율적인 검색, 삽입, 삭제 작업을 위해 트리 구조를 이용하며, 균형 트리는 트리의 깊이를 최소화하여 빠른 데이터 접근을 가능하게 합니다. MySQL에서 B트리(B-Tree)를 인텍스 테이블을 만들어 관리하는데 사용됩니다. MySQL의 InnoDB 스토리지 엔진은 기본적으로 B+트리를 사용하며 B-Tree 작동 원리와 구조에서 차이가 있습니다.

## 3. B-Tree

![image](https://github.com/user-attachments/assets/3d07123e-db9b-4189-afb8-8520e983362a)
B-Tree는 기본적으로 루트 노드, 내부 노드, 리프 노드로 구분됩니다. 이중에 내부노드와 리프 노드 모드 데이터를 저장합니다. 각 노드에서 리프 노드는 별도 연결을 포함하지 않습니다. 따라서 검색의 경우 데이터가 내부 노드에 있을 때 더 빨리 검색할 수 있습니다.

### B-Tree 구현
#### 1. BTree.java
해당 인터페이스는 B-Tree와 B+Tree 모두에 대해 공통 기능을 적용하기 위한 인터페이스로 구성됩니다. 기본적으로 데이터 검색, 범위 검색과 데이터 추가, 트리 출력의 4 가지 기능 구현을 필요로 합니다.
```java
import java.util.ArrayList;

public interface BTree {	
	public ArrayList<Integer> print();
	public Integer search(int key);
	public ArrayList<Integer> search(int start,int end);
	public void add(int key);
}
```

#### 2. BMTree.java
BMTree는 B-Tree에 대한 구현입니다. 인터페이스로부터 받아온 기능들을 직접 구현합니다.
<details>
<summary>전체 코드</summary>
  
```java
public class BMTree implements BTree {
	private Node root;
	int degree;
	
	public BMTree(int degree) {
		this.root = null;
		this.degree = degree;
	}
	
  // 전체 트리의 값을 배열로 출력
  @Override
  public ArrayList<Integer> print() {
      ArrayList<Integer> result = new ArrayList<Integer>();
      if (root != null) {
          root.print(result);
      }
      return result;
  }
    
  // 특정 값을 찾는 메서드
  @Override
  public Integer search(int key) {
      if (root != null) {
          return root.search(key);
      }
      return null;
  }
  
  // 범위 검색
  @Override
  public ArrayList<Integer> search(int start, int end) {
      ArrayList<Integer> result = new ArrayList<>();
      if (root != null) {
          root.search(start, end, result);
      }
      return result;
  }
  
  // 값 추가
	public void add(int key) {
        if (root == null) {
            root = new Node(degree, true);
            root.setKey(0, key);
        } else {
            if (root.countKeys() == 2 * degree - 1) {
            	Node node = new Node(degree, false);
            	node.setChild(0,root);
            	node.split(0,root);

                int i = 0;
                if (node.getKey(0) < key) {
                    i++;
                }
                node.getChild(i).insert(key);
                root = node;
            } else {
                root.insert(key);
            }
        }
    }
  // Node Class
}
```
</details>

<details>
<summary>B-트리 <code>add</code> 메소드 설명</summary>

```java
// 값 추가
  public void add(int key) {
      // 루트 노드가 비어있는 경우
      if (root == null) {
          // 새 노드를 생성하고 루트로 설정
          root = new Node(degree, true);
          // 루트 노드에 첫 번째 키를 설정
          root.setKey(0, key);
      } else {
          // 루트 노드가 가득 찬 경우
          if (root.countKeys() == 2 * degree - 1) {
              // 새로운 내부 노드 생성
              Node node = new Node(degree, false);
              // 새로운 노드의 첫 번째 자식으로 기존 루트 설정
              node.setChild(0, root);
              // 루트 노드를 분할하여 새로운 내부 노드로 승격
              node.split(0, root);
  
              // 삽입할 키가 어느 자식 노드에 들어갈지 결정
              int i = 0;
              if (node.getKey(0) < key) {
                  i++; // 키 값이 새 루트 노드의 첫 번째 키보다 크면, 두 번째 자식으로 삽입
              }
              // 해당 자식 노드에 값을 삽입
              node.getChild(i).insert(key);
              // 새로운 노드를 루트로 설정
              root = node;
          } else {
              // 루트 노드에 값 삽입
              root.insert(key);
          }
      }
  }
```
**설명**
1. **루트 노드가 비어 있는 경우**:
   - 트리가 비어 있을 경우, 새로 루트 노드를 생성하고, 해당 노드에 첫 번째 `key` 값을 저장합니다.
   - 이 과정은 트리가 처음 시작되는 경우에 해당합니다.
2. **루트 노드가 가득 찬 경우**:
   - 루트 노드가 이미 꽉 차 있는 경우, 새로운 **내부 노드**를 생성하여 기존 루트 노드를 자식으로 설정합니다.
   - 새로운 내부 노드는 루트 노드를 분할(`split`)하여 키 값을 상위 노드로 승격시킵니다.
   - 분할 후, 삽입할 `key` 값을 적절한 자식 노드에 삽입하기 위해 자식 노드를 선택합니다. 이때, 새로운 루트 노드의 키 값을 기준으로 `key`가 삽입될 위치를 결정합니다.
3. **값 삽입**:
   - 선택된 자식 노드에 `key` 값을 삽입합니다.
   - 삽입 후, 새로운 노드를 루트로 설정하여 트리의 균형을 유지합니다.
4. **루트 노드가 가득 차지 않은 경우**:
   - 만약 루트 노드가 가득 차지 않은 상태라면, 그냥 루트 노드에 `key` 값을 삽입합니다.  
</details>

#### 3. Node Class
Node Class는 BMTree 내부 클래스입니다. 각 노드의 역할을 하면서 차수, 자식 배열과 키 배열 리프 여부를 포함하고 있습니다. 또한 검색과 삽입 분할에 대한 기능도 포함하고 있습니다.
<details>
<summary>전체 코드</summary>
  
```java
  class Node{
    private int degree; // 최소 차수
    private Node[] children; // 자식 노드 배열
    private int[] keys; // 키 배열
    private boolean isLeaf; // 리프 노드 여부
    
    public Node(int degree, boolean isLeaf) {
          this.degree = degree;
          this.isLeaf = isLeaf;
          this.keys = new int[2 * degree - 1];
          this.children = new Node[2 * degree];
      }	
    
    // 출력
    public void print(ArrayList<Integer> arr) {
      int keyCount = countKeys();
      for (int i = 0; i < keyCount; i++) {
              if (!isLeaf) {
                children[i].print(arr);
              }
              arr.add(keys[i]);
          }
          if (!isLeaf) {
            children[keyCount].print(arr);
          }
    }
    
      // 키 검색 기능
      public Integer search(int key) {
          int i = 0;
          while (i < countKeys() && keys[i] < key) i++;
          
          // 검색 결과 반환
          if (i < countKeys() && keys[i] == key) return keys[i]; 
          
          // 리프 노드에 도달했으므로 값이 없음
          if (isLeaf) return null; 
          
          // 자식 노드로 내려감
          return children[i].search(key); 
      }
      
      public void search(int start, int end, ArrayList<Integer> result) {
          int i = 0;
          while (i < countKeys() && keys[i] < start) i++;
          
    
          while (i < countKeys() && keys[i] <= end) {
              if (!isLeaf) children[i].search(start, end, result);            
              result.add(keys[i]);
              i++;
          }
    
          if (!isLeaf) children[i].search(start, end, result);
          
      }
		
    public void insert(int key) {
        int i = countKeys() - 1;  // 마지막 키 인덱스        
        if (isLeaf) {
            // 리프 노드에 삽입할 위치 찾기
            while (i >= 0 && keys[i] > key) {
                keys[i + 1] = keys[i];  // 오른쪽으로 키를 밀기
                i--;
            }
            keys[i + 1] = key;  // 키 삽입
        } else {
            // 자식 노드로 내려가서 삽입
            while (i >= 0 && keys[i] > key) {
                i--;
            }
            i++;  // 자식 노드 인덱스
            
            // 자식 노드가 가득 차 있으면 분리
            if (children[i].countKeys() == 2 * degree - 1) {
                split(i, children[i]);  // 자식 노드 분리
                if (keys[i] < key) {
                    i++;  // 분리 후, 키에 맞는 자식으로 이동
                }
            }
            
            // 적합한 자식 노드로 내려가서 삽입
            children[i].insert(key);
        }
    }
	    
    public void split(int i, Node y) {
        int t = degree;  // 최소 차수
        
        // 새로운 노드 z 생성
        Node z = new Node(degree, y.isLeaf());
        
        // y의 중간 키를 부모 노드로 이동
        for (int j = 0; j < t - 1; j++) {
            z.setKey(j, y.getKey(j + t));
            y.setKey(j + t, 0);  // 원래 노드에서 키 제거
        }
        
        // 자식 노드가 있다면 분리된 자식 노드를 z로 이동
        if (!y.isLeaf()) {
            for (int j = 0; j < t; j++) {
                z.setChild(j, y.getChild(j + t));
                y.setChild(j + t, null);  // 원래 노드에서 자식 제거
            }
        }
    
        // 부모 노드에 새로운 자식과 키 삽입
        for (int j = countKeys(); j >= i + 1; j--) {
            setChild(j + 1, getChild(j));
        }
        setChild(i + 1, z);  // z 자식 추가
        
        for (int j = countKeys() - 1; j >= i; j--) {
            setKey(j + 1, getKey(j));
        }
        setKey(i, y.getKey(t - 1));  // 부모에 중간 키 삽입
        y.setKey(t - 1, 0);  // y에서 중간 키 제거
    }
	    
		public boolean isLeaf() {
			return this.isLeaf;
		}		
		public Node getChild(int i) {
			return this.children[i];
		}		
		public void setChild(int i,Node node) {
			this.children[i] = node;
		}		
		public int getKey(int i) {
			return this.keys[i];
		}		
		public void setKey(int i,int key) {
			this.keys[i] = key;
		}		
    public int countKeys() {
        int count = 0;
        for (int key : keys) { if (key == 0) break; count++; }
        return count;
    }		 
	}
```
</details>

<details>  
<summary>B-트리 <code>insert</code> 메소드 설명</summary>  

```java
// 노드에 키 삽입
public void insert(int key) {
    int i = countKeys() - 1;  // 마지막 키의 인덱스

    // 리프 노드에 삽입
    if (isLeaf) {
        // 삽입할 위치를 찾기 위해 오른쪽으로 이동
        while (i >= 0 && keys[i] > key) {
            keys[i + 1] = keys[i];  // 기존 키를 오른쪽으로 이동
            i--;
        }
        // 새로운 키 삽입
        keys[i + 1] = key;
    } else {
        // 적절한 자식 노드를 찾기 위해 이동
        while (i >= 0 && keys[i] > key) {
            i--;
        }
        i++;  // 자식 노드 인덱스 결정

        // 자식 노드가 가득 찬 경우 분할
        if (children[i].countKeys() == 2 * degree - 1) {
            split(i, children[i]);  // 자식 노드 분할
            if (keys[i] < key) {
                i++;  // 분할 후 오른쪽 자식으로 이동
            }
        }
        // 선택된 자식 노드에 삽입
        children[i].insert(key);
    }
}
```
**설명**
1. **리프 노드 삽입**:  
   - 리프 노드에서 삽입 위치를 찾습니다.  
   - 기존 키를 오른쪽으로 이동시키고, 새로운 키를 해당 위치에 삽입합니다.  
2. **내부 노드 처리**:  
   - 삽입할 키에 맞는 자식 노드를 탐색합니다.  
   - 자식 노드가 가득 찬 경우 `split` 메소드를 호출해 노드를 분할합니다.  
   - 분할 이후, 삽입할 키가 오른쪽 자식으로 이동해야 할 경우 인덱스를 조정합니다.  
3. **재귀적 삽입**:  
   - 선택된 자식 노드로 이동하여 재귀적으로 키를 삽입합니다.  
</details>

<details>
<summary>B-트리 <code>split</code> 메소드 설명</summary>

```java
// 노드 분할
public void split(int i, Node y) {
    int t = degree;  // 최소 차수
    
    // 새로운 노드 z 생성
    Node z = new Node(degree, y.isLeaf());
    
    // y의 중간 키를 부모 노드로 이동
    for (int j = 0; j < t - 1; j++) {
        z.setKey(j, y.getKey(j + t));
        y.setKey(j + t, 0);  // 원래 노드에서 키 제거
    }
    
    // 자식 노드가 있다면 분리된 자식 노드를 z로 이동
    if (!y.isLeaf()) {
        for (int j = 0; j < t; j++) {
            z.setChild(j, y.getChild(j + t));
            y.setChild(j + t, null);  // 원래 노드에서 자식 제거
        }
    }

    // 부모 노드에 새로운 자식과 키 삽입
    for (int j = countKeys(); j >= i + 1; j--) {
        setChild(j + 1, getChild(j));
    }
    setChild(i + 1, z);  // z 자식 추가
    
    for (int j = countKeys() - 1; j >= i; j--) {
        setKey(j + 1, getKey(j));
    }
    setKey(i, y.getKey(t - 1));  // 부모에 중간 키 삽입
    y.setKey(t - 1, 0);  // y에서 중간 키 제거
}
```
**설명**
1. **새로운 노드 생성**:  
   - `split` 메소드는 부모 노드가 자식 노드에서 한 개 이상의 키를 분할하여 새로운 자식 노드를 만들 때 사용됩니다.  
   - `z`라는 새로운 노드를 생성하여 분리된 데이터를 이동합니다.
2. **중간 키 부모로 이동**:  
   - 분할할 때, `y`의 중간 키를 부모 노드로 승격시킵니다.  
   - 이 중간 키는 부모 노드의 자식 포인터와 키 배열에 삽입됩니다.
3. **자식 노드 이동**:  
   - `y`가 자식 노드를 가지고 있다면, 분할된 자식 노드들도 새로운 노드 `z`로 이동합니다.  
   - 자식 노드들에 대한 포인터를 `z`에 설정하고, `y`에서 해당 자식들을 삭제합니다.
4. **부모 노드에 자식 추가**:  
   - 부모 노드에 새로운 자식 노드를 추가하고, 기존 자식 노드들의 포인터를 오른쪽으로 밀어냅니다.
5. **키 이동 및 업데이트**:  
   - 부모 노드에 새로운 키를 추가하고, `y`에서 중간 키를 제거합니다.  
   - 부모 노드가 가득 차지 않도록 삽입된 키들이 올바르게 배치됩니다.
</details>


<details>
<summary>B-트리 <code>search</code> 메소드 설명</summary>

```java
// 키 검색 기능
public Integer search(int key) {
    int i = 0;
    while (i < countKeys() && keys[i] < key) i++;
    
    // 검색 결과 반환
    if (i < countKeys() && keys[i] == key) return keys[i]; 
    
    // 리프 노드에 도달했으므로 값이 없음
    if (isLeaf) return null; 
    
    // 자식 노드로 내려감
    return children[i].search(key); 
}
```
**설명**
1. **키 탐색**:  
   - 먼저, `keys` 배열을 순차적으로 비교하여 `key`를 찾습니다.  
   - `key`가 발견되면 해당 값을 반환합니다.

2. **리프 노드 체크**:  
   - 만약 `key`를 찾지 못했거나 리프 노드에 도달하면 `null`을 반환합니다.

3. **자식 노드로 이동**:  
   - `key`를 찾지 못하면, 적절한 자식 노드로 내려가서 재귀적으로 `search` 메소드를 호출하여 탐색을 계속합니다.
</details>
