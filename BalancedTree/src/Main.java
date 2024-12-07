import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Main {
	
	public static void main(String[] args) {
		final int DEGREE = 2; // 차수
		final int KEYS = 10; // 데이터 개수
		long startTime, endTime; // 시작 시간, 종료 시간

		HashSet<Integer> set = randomHash(KEYS); // 데이터 수 만큼 생성
		BTree btree = new BTree(DEGREE); // B-트리 생성

		// 선형 삽입
		int[] array = new int[KEYS];
	    startTime = System.nanoTime(); // 시작
	    int index = 0;
        for (int value : set) array[index++] = value; // 중복된 값 없이 배열에 삽입        
	    endTime = System.nanoTime(); // 종료
	    System.out.println("Linear Array Insertion Time: " + convertToMilliseconds(endTime - startTime) + " ms");
	    
	    // 선형 검색 - 배열 전체 반복
        startTime = System.nanoTime(); // 검색 시작 시간
        for (int target : array) {
            linearSearch(array, target); // 배열의 모든 값을 순차 검색
        }
        endTime = System.nanoTime(); // 검색 종료 시간
        System.out.println("Linear Array Search Time  : " + convertToMilliseconds(endTime - startTime) + " ms");
        
	    // 선형 범위 검색 - 배열 전체 반복
        startTime = System.nanoTime(); // 검색 시작 시간
        linearRangeSearch(array,(int)KEYS/4*1,(int)KEYS/4*3);
        endTime = System.nanoTime(); // 검색 종료 시간
        System.out.println("Linear Array Range Search Time  : " + convertToMilliseconds(endTime - startTime) + " ms");
		    
	    // B-Tree 삽입
	    startTime = System.nanoTime(); // 시작
	    for (int value : set) btree.insert(value); // 중복된 값이 아니면 추가	    
	    endTime = System.nanoTime(); // 종료
	    System.out.println("B-Tree Insertion Time: " + convertToMilliseconds(endTime - startTime) + " ms");
	    
	    // B-Tree 검색
	    startTime = System.nanoTime();
	    for (int i = 0; i < KEYS; i++) btree.search(i);
	    endTime = System.nanoTime();
	    System.out.println("B-Tree Search Time: " + convertToMilliseconds(endTime - startTime) + " ms");
	    
	    // B-Tree 범위 검색 (전체 1/4부터 3/4까지)
	    startTime = System.nanoTime();
	    btree.search((int)KEYS/4*1,(int)KEYS/4*3);
	    endTime = System.nanoTime();
	    System.out.println("B-Tree Range Search Time: " + convertToMilliseconds(endTime - startTime) + " ms");
	    
	    // B-Tree 수정
	    startTime = System.nanoTime(); // 시작
	    for (int value : set) btree.update(value,value+KEYS); // 중복된 값이 아니면 추가
	    endTime = System.nanoTime(); // 종료
	    System.out.println("B-Tree Update Time: " + convertToMilliseconds(endTime - startTime) + " ms");
	    
	    // B-Tree 삭제
	    startTime = System.nanoTime(); // 시작
	    for (int value : set) btree.remove(value+KEYS); // 중복된 값이 아니면 추가
	    endTime = System.nanoTime(); // 종료
	    System.out.println("B-Tree Deletion Time: " + convertToMilliseconds(endTime - startTime) + " ms");	    
	    System.out.println("___________________________________________");
	}
	
    // 시간 변환을 위한 메서드
    private static double convertToMilliseconds(long nanoseconds) {
        return nanoseconds / 1000000.0;
    }
    
    private static HashSet<Integer> randomHash(int n) {
    	HashSet<Integer> set = new HashSet<>();    	
    	Random rand = new Random();
    	// 데이터 삽입 (랜덤 데이터, 중복 방지)
        while (set.size() < n) {  // 중복을 방지하면서 데이터를 삽입
            int value = rand.nextInt(n)+1;
            set.add(value);  // 중복된 값이 없으면 set에 추가
        }
		return set;
    }
    
    // 선형 검색
    private static void linearSearch(int[] array, int target) {
        for (int value : array) if (value == target) break; // 값이 발견되면 검색 중단
    }
    
    // 선형 검색
    private static List<Integer> linearRangeSearch(int[] array, int min, int max) {
        List<Integer> result = new ArrayList<>();
        for (int value : array) if (value >= min && value <= max) result.add(value);           
        return result;
    }
}
