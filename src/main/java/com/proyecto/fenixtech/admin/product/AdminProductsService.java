package com.proyecto.fenixtech.admin.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Products;
import com.proyecto.fenixtech.model.ProductsImg;
import com.proyecto.fenixtech.model.enums.ProductStatus;

@Service
public class AdminProductsService {

    @Autowired
    private AdminProductsRepository adminProductsRepository;

    @Autowired
    private AdminProductsImgRepository adminProductsImgRepository;

    @Transactional(readOnly = true)
    public List<Products> findProductsForAdmin(ProductStatus status, Integer subcategoryId, Integer companyId, String title) {
        String statusStr = (status != null) ? status.name() : null;
        return adminProductsRepository.findProductsAdminFilters(statusStr, subcategoryId, companyId, title);
    }

    @Transactional
    public void hideProduct(Integer id) {
        Products product = adminProductsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        
        // Se borra de los carritos activos por seguridad
        adminProductsRepository.deleteCartItemsByProductId(id);

        // Se oculta
        product.setProductStatus(ProductStatus.HIDDEN);
        adminProductsRepository.save(product);
    }

    @Transactional
    public void unhideProduct(Integer id) {
        Products product = adminProductsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        
        // Lógica automática de stock
        if (product.getStock() > 0) {
            product.setProductStatus(ProductStatus.ACTIVE);
        } else {
            product.setProductStatus(ProductStatus.SOLD_OUT);
        }
        
        adminProductsRepository.save(product);
    }

    @Transactional
    public void deleteProductImage(Integer imgId) {
        ProductsImg img = adminProductsImgRepository.findById(imgId)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada con id: " + imgId));
        
        adminProductsImgRepository.delete(img);
    }
}