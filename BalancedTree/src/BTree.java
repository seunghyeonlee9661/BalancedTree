import java.util.HashSet;
import java.util.Stack;

public class BTree {
	private int degree; // 차수
	private Node root;

	public BTree(int degree) {
		this.degree = degree;
		root = new Node();
	}

	public class Node {
		int count = 0; // 키 카운트
		int key[] = new int[2 * degree - 1]; // 키 배열
		Node child[] = new Node[2 * degree]; // 자식 노드 배열
		boolean leaf = true; // 리프

		public int Find(int key) { // 배열 내 키 확인
			for (int i = 0; i < this.count; i++) {
				if (this.key[i] == key) {
					return i;
				}
			}
			return -1;
		};
	}

	// 검색 기능 - 공개
	public int search(int key) {
		return search(root, key);
	}

	// 검색 기능 - 내부 구현
	private int search(Node node, int key) {
		int index = 0;
		if (node == null)
			return -1; // 검색 불가
		for (index = 0; index < node.count; index++) { // 노드의 각 키 배열을 돌면서 확인
			if (key < node.key[index])
				break;
			if (key == node.key[index])
				return node.key[index]; // 값에 해당하는 노드 확인!
		}
		if (node.leaf)
			return -1; // 리프인 경우 미발견
		else
			return search(node.child[index], key); // 재귀적 검색을 넘어감
	}

	// 노드 검색 기능 - 내부 구현 (노드 반환을 위해 추가)
	private Node searchNode(Node x, int key) {
		int i = 0;
		if (x == null)
			return x;
		for (i = 0; i < x.count; i++) {
			if (key < x.key[i])
				break;
			if (key == x.key[i])
				return x; // 값에 해당하는 노드 확인!
		}
		if (x.leaf) {
			return null;
		} else {
			return searchNode(x.child[i], key);
		}
	}

	// 분할 - left를 분할하여 parent의 왼쪽과 오른쪽 자식으로 추가
	private void split(Node parent, int pos, Node left) {
		Node right = new Node();
		right.leaf = left.leaf; // 리프 여부를 그대로 넘겨 받음
		right.count = degree - 1; // 키 개수 생성

		// left의 후반 노드를 right로 넘겨줌
		for (int j = 0; j < degree - 1; j++) {
			right.key[j] = left.key[j + degree];
		}

		// 리프가 아닌 경우 자식도 넘겨줌
		if (!left.leaf) {
			for (int j = 0; j < degree; j++) {
				right.child[j] = left.child[j + degree];
			}
		}

		left.count = degree - 1; // 키 개수 설정

		// 부모 노드의 자식 노드를 한칸씩 밀기
		for (int j = parent.count; j >= pos + 1; j--) {
			parent.child[j + 1] = parent.child[j];
		}
		// 우측 자식을 삽입
		parent.child[pos + 1] = right;

		// 부모 노드 키 배열을 한킨씩 밀기
		for (int j = parent.count - 1; j >= pos; j--) {
			parent.key[j + 1] = parent.key[j];
		}
		parent.key[pos] = left.key[degree - 1]; // 중간값 부모 노드에 삽입

		parent.count = parent.count + 1; // 부모 노드 키 개수 증가
	}

	// 삽입 기능 - 공개
	public void insert(final int key) {
		Node node = root;
		if (node.count == 2 * degree - 1) { // 루트가 가득찬 경우 -> 기존 루트를 자식으로 넘기고 분할 후 추가
			Node newRoot = new Node(); // 새로운 노드 생성
			root = newRoot;
			newRoot.leaf = false;
			newRoot.child[0] = node;
			split(newRoot, 0, node); // 분할
			insert(newRoot, key); // 새 루트에 값 추가 과정 진행
		} else {
			insert(node, key); // 루트가 빈 경우 -> 추가
		}
	}

	// 삽입 기능 - 내부 구현
	final private void insert(Node node, int key) {
		if (node.leaf) { // 노드가 리프인 경우
			int i = 0;
			// 키보다 큰 값들을 이동
			for (i = node.count - 1; i >= 0 && key < node.key[i]; i--) {
				node.key[i + 1] = node.key[i];
			}
			// 위치에 키를 추가
			node.key[i + 1] = key;
			// 개수 1 추가
			node.count = node.count + 1;
		} else { // 노드가 내부인 경우
			int i = 0;
			// 노드에서 탐색
			while (i < node.count && key >= node.key[i]) {
				i++;
			}
			// 해당 자식 노드를 임시 저장
			Node tmp = node.child[i];
			// 자식 노드가 가득 찬 경우 분할 과정 진행
			if (tmp.count == 2 * degree - 1) {
				split(node, i, tmp);
				if (key > node.key[i])
					i++; // 분할 후 삽입 키와 중간 값 비교 후 이동

			}
			insert(node.child[i], key); // 해당 위치에 자식 노드 삽입
		}
	}

	// 삭제 기능 - 공개
	public void remove(int key) {
		Node node = searchNode(root, key);
		if (node == null) { // 노드가 없는 경우 삭제 과정 생략!
			return;
		}
		remove(root, key);
	}

	// 삭제 기능 - 내부 구현
	private void remove(Node node, int key) {
		int pos = node.Find(key); // 해당 키의 위치를 찾는다.

		if (pos != -1) { // 키가 노드에 존재하는 경우
			if (node.leaf) { // 리프 노드인 경우
				int i = 0;
				for (i = 0; i < node.count && node.key[i] != key; i++) {
					// 키를 찾아 삭제할 위치를 찾는다.
				}
				// 삭제된 키 뒤의 값을 한 칸씩 왼쪽으로 이동
				for (; i < node.count - 1; i++) {
					node.key[i] = node.key[i + 1];
				}
				node.count--; // 노드의 키 개수를 하나 줄인다
				return;
			}
			if (!node.leaf) { // 리프가 아닌 내부 노드인 경우
				Node pred = node.child[pos]; // 해당 자식 노드를 pred로 설정
				int predKey = 0;

				// pred가 차수가 충분한 경우
				if (pred.count >= degree) {
					while (true) {
						if (pred.leaf) { // pred가 리프 노드라면 마지막 키를 가져옴
							predKey = pred.key[pred.count - 1];
							break;
						} else {
							pred = pred.child[pred.count]; // pred의 자식으로 내려감
						}
					}
					remove(pred, predKey); // pred에서 키를 삭제하고
					node.key[pos] = predKey; // 해당 부모 노드의 키를 predKey로 변경
					return;
				}

				// 다음 자식 노드가 차수가 충분한 경우
				Node nextNode = node.child[pos + 1];
				if (nextNode.count >= degree) {
					int nextKey = nextNode.key[0];
					if (!nextNode.leaf) {
						nextNode = nextNode.child[0]; // nextNode가 리프가 아니라면 가장 왼쪽 자식으로 내려감
						while (true) {
							if (nextNode.leaf) { // nextNode가 리프라면 마지막 키를 가져옴
								nextKey = nextNode.key[nextNode.count - 1];
								break;
							} else {
								nextNode = nextNode.child[nextNode.count]; // 자식을 따라 내려감
							}
						}
					}
					remove(nextNode, nextKey); // nextNode에서 키를 삭제하고
					node.key[pos] = nextKey; // 해당 부모 노드의 키를 nextKey로 변경
					return;
				}

				// pred와 nextNode의 키를 합쳐서 하나의 노드로 합친다
				int temp = pred.count + 1;
				pred.key[pred.count++] = node.key[pos]; // 부모 노드에서 가져온 키를 pred에 추가
				for (int i = 0, j = pred.count; i < nextNode.count; i++) {
					pred.key[j++] = nextNode.key[i]; // nextNode의 키를 pred로 이동
					pred.count++;
				}
				for (int i = 0; i < nextNode.count + 1; i++) {
					pred.child[temp++] = nextNode.child[i]; // nextNode의 자식도 pred로 이동
				}

				// 부모 노드에서 해당 자식 정보를 삭제하고 나머지 키와 자식을 이동
				node.child[pos] = pred;
				for (int i = pos; i < node.count - 1; i++) {
					node.key[i] = node.key[i + 1];
				}
				for (int i = pos + 1; i < node.count; i++) {
					node.child[i] = node.child[i + 1];
				}
				node.count--; // 부모 노드의 키 개수 감소

				// 만약 부모 노드의 키 개수가 0이라면, 루트가 변경되므로 루트를 새로 설정
				if (node.count == 0) {
					if (node == root) {
						root = node.child[0];
					}
					node = node.child[0];
				}

				remove(pred, key); // pred에서 키를 삭제
				return;
			}
		} else { // 키가 노드에 존재하지 않는 경우
			for (pos = 0; pos < node.count; pos++) {
				if (node.key[pos] > key) { // key가 클 때까지 찾는다.
					break;
				}
			}
			Node tmp = node.child[pos]; // 해당 자식 노드를 tmp로 설정
			if (tmp.count >= degree) {
				remove(tmp, key); // 자식 노드에서 키를 삭제
				return;
			}

			// 자식 노드들이 차수가 충분하지 않으면 병합이나 키 이동을 한다.
			if (true) {
				Node nb = null;
				int devider = -1;

				// 다음 자식 노드가 차수가 충분하면 키를 이동
				if (pos != node.count && node.child[pos + 1].count >= degree) {
					devider = node.key[pos]; // 부모 노드의 키를 가져옴
					nb = node.child[pos + 1]; // 다음 자식 노드를 nb로 설정
					node.key[pos] = nb.key[0]; // 부모 노드의 키를 다음 자식의 첫 번째 키로 바꿈
					tmp.key[tmp.count++] = devider; // tmp 자식 노드에 해당 키를 추가
					tmp.child[tmp.count] = nb.child[0]; // 자식도 이동
					for (int i = 1; i < nb.count; i++) {
						nb.key[i - 1] = nb.key[i]; // 나머지 키를 왼쪽으로 이동
					}
					for (int i = 1; i <= nb.count; i++) {
						nb.child[i - 1] = nb.child[i]; // 자식도 왼쪽으로 이동
					}
					nb.count--; // nb의 키 개수를 하나 줄임
					remove(tmp, key); // tmp에서 키를 삭제
					return;
				} else if (pos != 0 && node.child[pos - 1].count >= degree) { // 이전 자식 노드가 차수가 충분하면
					devider = node.key[pos - 1]; // 부모 노드의 키를 가져옴
					nb = node.child[pos - 1]; // 이전 자식 노드를 nb로 설정
					node.key[pos - 1] = nb.key[nb.count - 1]; // 부모 노드의 키를 이전 자식의 마지막 키로 바꿈
					Node child = nb.child[nb.count]; // 이전 자식의 마지막 자식
					nb.count--; // nb의 키 개수를 하나 줄임

					// tmp에 키를 왼쪽으로 이동
					for (int i = tmp.count; i > 0; i--) {
						tmp.key[i] = tmp.key[i - 1];
					}
					tmp.key[0] = devider; // tmp 자식의 첫 번째 키에 devider를 추가
					for (int i = tmp.count + 1; i > 0; i--) {
						tmp.child[i] = tmp.child[i - 1];
					}
					tmp.child[0] = child; // tmp의 첫 번째 자식으로 child를 추가
					tmp.count++; // tmp의 키 개수를 증가
					remove(tmp, key); // tmp에서 키를 삭제
					return;
				} else { // 둘 다 차수가 부족하면 병합
					Node left = null;
					Node right = null;
					if (pos != node.count) { // 마지막 자식 노드가 아니면
						devider = node.key[pos]; // 부모 노드에서 키를 가져옴
						left = node.child[pos]; // lt로 설정
						right = node.child[pos + 1]; // rt로 설정
					} else { // 마지막 자식 노드라면
						devider = node.key[pos - 1]; // 부모 노드에서 키를 가져옴
						right = node.child[pos]; // rt로 설정
						left = node.child[pos - 1]; // lt로 설정
						pos--; // 마지막 자식 노드이므로 pos를 하나 감소
					}

					// 부모 노드에서 해당 자식의 키와 자식 노드를 삭제
					for (int i = pos; i < node.count - 1; i++) {
						node.key[i] = node.key[i + 1];
					}
					for (int i = pos + 1; i < node.count; i++) {
						node.child[i] = node.child[i + 1];
					}
					node.count--; // 부모 노드의 키 개수를 줄임

					left.key[left.count++] = devider; // lt에 부모 노드에서 가져온 키 추가

					// rt의 키와 자식을 lt로 이동
					for (int i = 0, j = left.count; i < right.count + 1; i++, j++) {
						if (i < right.count) {
							left.key[j] = right.key[i];
						}
						left.child[j] = right.child[i];
					}
					left.count += right.count; // lt의 키 개수 업데이트
					if (node.count == 0) { // 부모 노드의 키 개수가 0이면 루트 노드를 업데이트
						if (node == root) {
							root = node.child[0];
						}
						node = node.child[0];
					}
					remove(left, key); // lt에서 키 삭제
					return;
				}
			}
		}
	}

	public void task(int a, int b) {
		Stack<Integer> st = new Stack<>();
		findKeys(a, b, root, st);
		while (st.isEmpty() == false) {
			this.remove(root, st.pop());
		}
	}

	private void findKeys(int a, int b, Node x, Stack<Integer> st) {
		int i = 0;
		for (i = 0; i < x.count && x.key[i] < b; i++) {
			if (x.key[i] > a) {
				st.push(x.key[i]);
			}
		}
		if (!x.leaf) {
			for (int j = 0; j < i + 1; j++) {
				findKeys(a, b, x.child[j], st);
			}
		}
	}

	public void show() {
		show(root, 0);
	}

	// Show the node
	private void show(Node x, int level) {
		if (x == null) {
			return; // null이면 아무것도 출력하지 않음
		}

		// 현재 레벨과 노드 키를 출력, 중복 키는 하나로 묶음
		System.out.print("Level " + level + ": ");
		HashSet<Integer> uniqueKeys = new HashSet<>();

		for (int i = 0; i < x.count; i++) {
			uniqueKeys.add(x.key[i]); // 중복 제거를 위해 HashSet 사용
		}

		// HashSet을 이용해 중복 없는 키들을 출력
		for (Integer key : uniqueKeys) {
			System.out.print(key + " ");
		}
		System.out.println(); // 한 줄 끝내기

		// 자식 노드가 있으면 그 자식들을 출력 (하위 레벨로)
		if (!x.leaf) {
			for (int i = 0; i < x.count + 1; i++) {
				show(x.child[i], level + 1);
			}
		}
	}

}