package com.company.project.uber.uperApp.strategies;

import com.company.project.uber.uperApp.strategies.Impl.DriverMatchingHighestRatedDriverStrategy;
import com.company.project.uber.uperApp.strategies.Impl.DriverMatchingNearestDriverStrategy;
import com.company.project.uber.uperApp.strategies.Impl.RideFareSurgePricingFareCalculationStrategy;
import com.company.project.uber.uperApp.strategies.Impl.RiderFareDefaultFareCalculationStrategy;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class RideStrategyManager {

    private final DriverMatchingHighestRatedDriverStrategy highestRatedDriverStrategy;
    private final DriverMatchingNearestDriverStrategy nearestDriverStrategy;
    private final RideFareSurgePricingFareCalculationStrategy surgePricingFareCalculationStrategy;
    private final RiderFareDefaultFareCalculationStrategy defaultFareCalculationStrategy;

    public DriverMatchingStrategy driverMatchingStrategy(double riderRating) {
        if (riderRating >= 4.8) {
            return highestRatedDriverStrategy;
        } else {
            return nearestDriverStrategy;
        }
    }


    public RideFareCalculationStrategy rideFareCalculationStrategy() {
        // 6PM to 9PM is SURGE TIME
        LocalTime surgeStartTime = LocalTime.of(18, 0);
        LocalTime surgeEndTime = LocalTime.of(21, 0);
        LocalTime currentTime = LocalTime.now();

        boolean isSurgeTime = currentTime.isAfter(surgeStartTime) && currentTime.isBefore(surgeEndTime);

        if (isSurgeTime) {
            return surgePricingFareCalculationStrategy;
        } else {
            return defaultFareCalculationStrategy;
        }
    }
}
