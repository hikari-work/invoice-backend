package org.yann.integerasiorderkuota.bot.service.product;

import org.springframework.stereotype.Service;
import org.yann.integerasiorderkuota.bot.entity.CustomerState;
import org.yann.integerasiorderkuota.bot.entity.Draft;
import org.yann.integerasiorderkuota.bot.entity.Product;
import org.yann.integerasiorderkuota.bot.entity.UserSession;
import org.yann.integerasiorderkuota.bot.repository.ProductRepository;
import org.yann.integerasiorderkuota.bot.service.SessionManager;



@Service
public class ProductService {

    private final SessionManager userSessions;
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository, SessionManager userSessions) {
        this.productRepository = productRepository;
        this.userSessions = userSessions;
    }

   public CustomerState getCustomerState(String userId, String messageId) {
       if (userSessions.getSession(userId).containsKey(userId)) {
           return userSessions.getSession(userId).get(userId).getCustomerState() ;
       }
       userSessions.getSession(userId).computeIfAbsent(userId, id ->
               UserSession.builder()
                       .customerState(CustomerState.IDLE)
                       .messageId(messageId)
                       .userId(userId)
                       .build()
       );
       return userSessions.getSession(userId).get(userId).getCustomerState();

   }


   public void saveProductDetails(String userId, CustomerState customerState, String messageId, String productDetails) {
       switch (customerState) {
           case ADD_PRODUCT_NAME -> userSessions.getSession(userId)
                   .computeIfPresent(userId, (id, userSession) -> {
                       Draft draft = userSession.getDraft();
                       if (draft instanceof Product product) {
                           product.setName(productDetails);
                       } else {
                           throw new IllegalStateException("Draft bukan Product!");
                       }
                       userSession.setCustomerState(CustomerState.ADD_PRODUCT_DESCRIPTION);
                       userSession.setMessageId(messageId);

                       return userSession;
                   });
           case ADD_PRODUCT_DESCRIPTION -> userSessions.getSession(userId)
                   .computeIfPresent(userId, (id, userSession) -> {
                       Draft draft = userSession.getDraft();
                       if (draft instanceof Product product) {
                           product.setDescription(productDetails);
                       } else {
                           throw new IllegalStateException("Draft bukan Product!");
                       }
                       userSession.setCustomerState(CustomerState.ADD_PRODUCT_PRICE);
                       userSession.setMessageId(messageId);

                       return userSession;
                   });
           case ADD_PRODUCT_IMAGE -> userSessions.getSession(userId)
                   .computeIfPresent(userId, (id, userSession) -> {
                       Draft draft = userSession.getDraft();
                       if (draft instanceof Product product) {
                           product.setImage(productDetails);
                       } else {
                           throw new IllegalStateException("Draft bukan Product!");
                       }
                       userSession.setCustomerState(CustomerState.IDLE);
                       saveProduct(product);
                       return null;
                   });
       }

   }

   public Product saveProduct(Product product) {
       return productRepository.save(product);
   }

}
