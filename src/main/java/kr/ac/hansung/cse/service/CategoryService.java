package kr.ac.hansung.cse.service;

import kr.ac.hansung.cse.exception.DuplicateCategoryException;
import kr.ac.hansung.cse.model.Category;
import kr.ac.hansung.cse.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly=true)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category resolveCategory(String categoryName) {
        if (categoryName == null || categoryName.isBlank()) return null;
        return categoryRepository.findByName(categoryName).orElse(null);
    }

    // 전체 카테고리 조회
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // ID로 카테고리 조회
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @Transactional // readOnly = false (쓰기 가능)
    public Category createCategory(String name) {
        // 빈칸 or null 입력 검사
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("카테고리 이름이 비어 있습니다.");
        }
        // 중복 검사: 이름이 이미 있으면 예외 발생
        categoryRepository.findByName(name)
                .ifPresent(c -> { throw new DuplicateCategoryException(name); });
        return categoryRepository.save(new Category(name));
    }

    @Transactional
    public void deleteCategory(Long id) {

        long count = categoryRepository.countProductsByCategoryId(id);

        if (count > 0) {
            throw new IllegalStateException(
                    "상품 " + count + "개가 연결되어 있어 삭제할 수 없습니다.");
        }

        categoryRepository.delete(id);
    }
}
