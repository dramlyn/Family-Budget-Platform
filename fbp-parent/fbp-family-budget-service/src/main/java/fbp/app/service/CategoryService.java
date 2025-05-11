package fbp.app.service;

import fbp.app.dto.category.AddCategoryDtoRequest;
import fbp.app.dto.category.CategoryDto;
import fbp.app.exception.UserServiceException;
import fbp.app.mapper.CategoryMapper;
import fbp.app.model.Category;
import fbp.app.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryDto addCategory(AddCategoryDtoRequest request){
        Category category = CategoryMapper.toCategory(request);

        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    public CategoryDto getCategory(Long id){
        return CategoryMapper.toCategoryDto(categoryRepository.findById(id).orElseThrow(() -> new UserServiceException(String.format("Category with id %s not found", id), HttpStatus.NOT_FOUND)));
    }

    public List<CategoryDto> getAllCategories(){
        return categoryRepository.findAll().stream().map(CategoryMapper::toCategoryDto).toList();
    }

    public void deleteCategory(Long id){
        try{
            categoryRepository.deleteById(id);
        } catch (Exception e){
            throw new UserServiceException(String.format("Error when delete category with id %s : %s", id, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
