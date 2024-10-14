package mul.cam.e.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mul.cam.e.dao.MyAssetDao;
import mul.cam.e.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssetComparisonService {

    private final MyAssetDao myAssetDao;
    private final MyAssetService myAssetService;

    public Map<String, Double> getPeerAverageAssets(int userId) {
        log.info("Getting peer average assets for user ID: {}", userId);
        List<UserDto> peerUsers = myAssetDao.getPeerUsersByAge(userId);
        log.info("Number of peer users: {}", peerUsers.size());

        double totalBankBalance = 0;
        double totalStockTotal = 0;
        double totalCar = 0;
        double totalRealEstate = 0;

        for (UserDto peerUser : peerUsers) {
            int peerUserId = peerUser.getUserId();
            try {
                double bankBalance = myAssetDao.getBankBalanceByUserId(peerUserId);
                double stockTotal = myAssetDao.getStockTotalByUserId(peerUserId);
//                double carValue = myAssetDao.getCarByUserId(peerUserId);
                int carValue = myAssetService.totalCar(peerUserId);
//                double realEstateValue = myAssetDao.getRealestateByUserId(peerUserId);
                int realEstateValue = myAssetService.totalRealestate(peerUserId);

                log.info("Peer user {} assets - Bank: {}, Stock: {}, Car: {}, RealEstate: {}",
                        peerUserId, bankBalance, stockTotal, carValue, realEstateValue);

                totalBankBalance += bankBalance;
                totalStockTotal += stockTotal;
                totalCar += carValue;
                totalRealEstate += realEstateValue;
            } catch (Exception e) {
                log.error("Error calculating assets for peer user {}: {}", peerUserId, e.getMessage());
            }
        }

        int totalPeers = Math.max(peerUsers.size(), 1);

        Map<String, Double> averageAssets = new HashMap<>();
        averageAssets.put("Bank Balance", totalBankBalance / totalPeers);
        averageAssets.put("Stock Total", totalStockTotal / totalPeers);
        averageAssets.put("Car", totalCar / totalPeers);
        averageAssets.put("Real Estate", totalRealEstate / totalPeers);

        log.info("Calculated average assets: {}", averageAssets);
        return averageAssets;
    }

    public Map<String, Double> getUserTotalAssets(int userId) {
        log.info("Getting total assets for user ID: {}", userId);
        Map<String, Double> userAssets = new HashMap<>();
        try {
            userAssets.put("Bank Balance", myAssetDao.getBankBalanceByUserId(userId));
            userAssets.put("Stock Total", myAssetDao.getStockTotalByUserId(userId));
            userAssets.put("Car", myAssetDao.getCarByUserId(userId));
            userAssets.put("Real Estate", myAssetDao.getRealestateByUserId(userId));
            log.info("User total assets: {}", userAssets);
        } catch (Exception e) {
            log.error("Error getting total assets for user {}: {}", userId, e.getMessage());
        }
        return userAssets;
    }
}
