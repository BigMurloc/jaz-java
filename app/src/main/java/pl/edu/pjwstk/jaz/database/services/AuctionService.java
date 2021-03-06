package pl.edu.pjwstk.jaz.database.services;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.jaz.controllers.requests.AuctionRequest;
import pl.edu.pjwstk.jaz.controllers.requests.ParameterRequest;
import pl.edu.pjwstk.jaz.controllers.requests.PhotoRequest;
import pl.edu.pjwstk.jaz.database.entities.*;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuctionService {

    private final EntityManager entityManager;
    private final ParameterService parameterService;
    private final CategoryService categoryService;

    public AuctionService(EntityManager entityManager, ParameterService parameterService, CategoryService categoryService) {
        this.entityManager = entityManager;
        this.parameterService = parameterService;
        this.categoryService = categoryService;
    }

    @Transactional
    public void addAuction(AuctionRequest auctionRequest){
        Auction auction = new Auction();
        auction = setUpAuction(auction, auctionRequest);
        entityManager.persist(auction);
    }

    @Transactional
    public void updateAuction(Auction auction){
        entityManager.merge(auction);
    }

    public Auction setUpAuction(Auction auction, AuctionRequest auctionRequest) {
        Category category = categoryService.findCategoryByName(auctionRequest.getCategory());
        User currentUser =
                (User) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();
        auction.setUser(currentUser);
        auction.setTitle(auctionRequest.getTitle());
        auction.setDescription(auctionRequest.getDescription());
        auction.setPrice(auctionRequest.getPrice());
        auction.setPhoto(addPhoto(auctionRequest.getPhotos()));
        auction.setCategory(category);
        auction.setAuctionParameters(addAuctionParameter(auctionRequest.getParameters(), auction));
        Long version = auction.getVersion();
        version++;
        auction.setVersion(version++);
        return auction;
    }

    public Auction findAuctionById(Long id){
        Auction auction = new Auction();
        return auction = (Auction) entityManager
                .createQuery("SELECT a FROM Auction a  WHERE a.id=:id")
                .setParameter("id", id)
                .getSingleResult();
    }

    //private------
    private List<Photo> addPhoto(List<PhotoRequest> photos){
        List<Photo> photoList = new ArrayList<>();
        for(PhotoRequest photo : photos){
            Photo photoEntity = new Photo();
            photoEntity.setLink(photo.getLink());
            photoEntity.setOrder(photo.getOrder());
            photoList.add(photoEntity);
        }
        return photoList;
    }

    private List<AuctionParameter> addAuctionParameter(List<ParameterRequest> parameters, Auction auction){
        List<AuctionParameter> auctionParameters = new ArrayList<>();

        for(ParameterRequest parameterRequest : parameters){
            AuctionParameter auctionParameter = new AuctionParameter();

            parameterService.addParameter(parameterRequest.getKey());
            Parameter parameter = parameterService.findParameterByKey(parameterRequest.getKey());

            auctionParameter.setAuction(auction);
            auctionParameter.setParameter(parameter);
            auctionParameter.setValue(parameterRequest.getValue());

            auctionParameters.add(auctionParameter);
        }

        return auctionParameters;
    }

}
