import java.util.HashSet;
import java.util.Random;

public class Main {
	public static void main(String[] args) {
        // 각 트리의 객체를 생성합니다.
        int degree = 3;  // 예시로 degree를 3으로 설정
        int n = 10000;  // 테스트할 데이터의 크기
        int rangeStart = 300;  // 범위 검색 시작 값
        int rangeEnd = 500;    // 범위 검색 종료 값

        // 랜덤 값 생성을 위한 세트와 랜덤 객체
        HashSet<Integer> set = new HashSet<>();  // 중복을 방지할 set
        Random rand = new Random();

        // 데이터 삽입 (랜덤 데이터, 중복 방지)
        while (set.size() < n) {  // 중복을 방지하면서 데이터를 삽입
            int value = rand.nextInt(n);
            set.add(value);  // 중복된 값이 없으면 set에 추가
        }

        // 작업 구분과 메모리 사용 체크 함수
        printMemoryUsage("Before Array Insertion");

        // 배열 삽입 시간
        long startTime = System.nanoTime();
        int[] array = new int[n];
        int index = 0;
        for (int value : set) {
            array[index++] = value;
        }
        long endTime = System.nanoTime();
        System.out.println("Array Insertion Time: " + convertToMilliseconds(endTime - startTime) + " ms");
        printMemoryUsage("After Array Insertion");

        // 배열 검색 시간
        startTime = System.nanoTime();
        for (int i = 0; i < n; i++) searchArray(array, i); // 배열에서 값 검색        
        endTime = System.nanoTime();
        System.out.println("Array Search Time: " + convertToMilliseconds(endTime - startTime) + " ms");

        // 배열 범위 검색 시간
        startTime = System.nanoTime();
        searchArrayRange(array, rangeStart, rangeEnd); // 배열에서 범위 검색
        endTime = System.nanoTime();
        System.out.println("Array Range Search Time: " + convertToMilliseconds(endTime - startTime) + " ms");
        System.out.println("\n----------------------------------------------------");

        // 작업 구분과 메모리 사용 체크 함수
        printMemoryUsage("Before B-Tree Insertion");

        // B-Tree 삽입 시간
        startTime = System.nanoTime();
        BMTree bMTree = new BMTree(degree);  // B-Tree 객체 생성
        for (int value : set) {
            bMTree.add(value);  // 중복된 값이 아니면 추가
        }
        endTime = System.nanoTime();
        System.out.println("B-Tree Insertion Time: " + convertToMilliseconds(endTime - startTime) + " ms");
        printMemoryUsage("After B-Tree Insertion");

        // B-Tree 검색 시간
        startTime = System.nanoTime();
        for (int i = 0; i < n; i++) bMTree.search(i);        
        endTime = System.nanoTime();
        System.out.println("B-Tree Search Time: " + convertToMilliseconds(endTime - startTime) + " ms");

        // B-Tree 범위 검색 시간
        startTime = System.nanoTime();
        bMTree.search(rangeStart, rangeEnd);  // 범위 검색
        endTime = System.nanoTime();
        System.out.println("B-Tree Range Search Time: " + convertToMilliseconds(endTime - startTime) + " ms");
        System.out.println("\n----------------------------------------------------");

        // 작업 구분과 메모리 사용 체크 함수
        printMemoryUsage("Before B+ Tree Insertion");

        // B+ Tree 삽입 시간
        startTime = System.nanoTime();
        BPTree bptree = new BPTree(degree);  // B+ Tree 객체 생성
        for (int value : set) {
            bptree.add(value);  // 중복된 값이 아니면 추가
        }
        endTime = System.nanoTime();
        System.out.println("B+ Tree Insertion Time: " + convertToMilliseconds(endTime - startTime) + " ms");
        printMemoryUsage("After B+ Tree Insertion");

        // B+ Tree 검색 시간
        startTime = System.nanoTime();
        for (int i = 0; i < n; i++) bptree.search(i);        
        endTime = System.nanoTime();
        System.out.println("B+ Tree Search Time: " + convertToMilliseconds(endTime - startTime) + " ms");

        // B+ Tree 범위 검색 시간
        startTime = System.nanoTime();
        bptree.search(rangeStart, rangeEnd);  // 범위 검색
        endTime = System.nanoTime();
        System.out.println("B+ Tree Range Search Time: " + convertToMilliseconds(endTime - startTime) + " ms");
        // 작업 구분
        System.out.println("\n----------------------------------------------------");
    }

    // 배열에서 값 검색
    private static void searchArray(int[] array, int target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) {
                return;
            }
        }
    }

    // 배열에서 범위 검색 (start와 end 사이의 값들을 찾는 예시)
    private static void searchArrayRange(int[] array, int start, int end) {
        for (int i = start; i <= end; i++) {
            searchArray(array, i);
        }
    }

    // 시간 변환을 위한 메서드
    private static double convertToMilliseconds(long nanoseconds) {
        return nanoseconds / 1000000.0;
    }

    // 메모리 사용량 출력 함수
    private static void printMemoryUsage(String message) {
        Runtime runtime = Runtime.getRuntime();
        long memoryUsed = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("\n" + message + " | Memory used: " + memoryUsed / 1024 / 1024 + " MB");
    }
}
