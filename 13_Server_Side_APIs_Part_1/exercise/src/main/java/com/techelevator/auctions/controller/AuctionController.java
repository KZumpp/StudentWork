package com.techelevator.auctions.controller;

import com.techelevator.auctions.dao.AuctionDao;
import com.techelevator.auctions.dao.MemoryAuctionDao;
import com.techelevator.auctions.model.Auction;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.techelevator.auctions.dao.MemoryAuctionDao.auctions;

@RestController
@RequestMapping("/auctions")
public class AuctionController {

    private AuctionDao dao;

    public AuctionController() {
        this.dao = new MemoryAuctionDao();
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Auction> list(@RequestParam(name = "title_like", defaultValue = "") String searchTerm,
                              @RequestParam(name = "currentBid_lte", defaultValue = "0.00") double maxPrice) {
        List<Auction> matchTitles = new ArrayList<>();
        List<Auction> matchPrices = new ArrayList<>();
        List<Auction> auctionsByBoth = new ArrayList<>();

        if(maxPrice > 0.00 && !searchTerm.isEmpty()) {
            for (Auction auction : auctions) {
                if (auction.getCurrentBid() <= maxPrice && auction.getTitle().toLowerCase().contains(searchTerm.toLowerCase())) {
                    auctionsByBoth.add(auction);
                }
            }
            return auctionsByBoth;
        } else if (!searchTerm.isEmpty()) {
            for (Auction auction : auctions) {
                if (auction.getTitle().toLowerCase().contains(searchTerm.toLowerCase())) {
                    matchTitles.add(auction);
                }
            }
            return matchTitles;
        } else if (maxPrice > 0.00) {
            for (Auction auction : auctions) {
                if (auction.getCurrentBid() <= maxPrice) {
                    matchPrices.add(auction);
                }
            }
            return matchPrices;

        }

        return auctions;

    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Auction get(@PathVariable int id) {
        for (Auction auction : auctions) {
            if (auction.getId() == id) {
                return auction;
            }
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Auction create(@RequestBody Auction auction) {
        auction.setId(auctions.size() + 1);
        auctions.add(auction);
        return auction;
    }


}
