package kr.ac.hansung.cse.exception;

// 카테고리 명 중복 예외처리
public class DuplicateCategoryException extends RuntimeException {

    public DuplicateCategoryException(String name) {
        super("이미 존재하는 카테고리입니다: " + name);
    }
}