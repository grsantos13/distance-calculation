package io.github.grsantos13.distancecalculation.resources;

import io.github.grsantos13.distancecalculation.models.DistanceRespose;
import io.github.grsantos13.distancecalculation.models.Unit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DistanceResourceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Should calculate distance between São Paulo and Rio de Janeiro")
    void shouldCalculateDistanceBetweenSaoPauloAndRioDeJaneiro() {
        // São Paulo coordinates: -23.5505, -46.6333
        // Rio de Janeiro coordinates: -22.9068, -43.1729
        // Expected distance: approximately 357-359 km

        String url = "/distances?latitudeStart=-23.5505&longitudeStart=-46.6333&latitudeEnd=-22.9068&longitudeEnd=-43.1729&unit=KM";
        
        ResponseEntity<DistanceRespose> response = restTemplate.getForEntity(url, DistanceRespose.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().unit()).isEqualTo(Unit.KM);
        
        // The actual distance is approximately 357-362 km
        // Due to rounding up (CEILING), we expect the distance to be in this range
        BigDecimal distance = response.getBody().distance();
        assertThat(distance).isGreaterThanOrEqualTo(new BigDecimal("357"));
        assertThat(distance).isLessThanOrEqualTo(new BigDecimal("365"));
    }

    @Test
    @DisplayName("Should return zero distance for identical coordinates")
    void shouldReturnZeroDistanceForIdenticalCoordinates() {
        // Same location: -23.5505, -46.6333
        String url = "/distances?latitudeStart=-23.5505&longitudeStart=-46.6333&latitudeEnd=-23.5505&longitudeEnd=-46.6333&unit=KM";
        
        ResponseEntity<DistanceRespose> response = restTemplate.getForEntity(url, DistanceRespose.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().unit()).isEqualTo(Unit.KM);
        assertThat(response.getBody().distance()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Should calculate distance in meters")
    void shouldCalculateDistanceInMeters() {
        // São Paulo to Rio de Janeiro in meters
        String url = "/distances?latitudeStart=-23.5505&longitudeStart=-46.6333&latitudeEnd=-22.9068&longitudeEnd=-43.1729&unit=M";
        
        ResponseEntity<DistanceRespose> response = restTemplate.getForEntity(url, DistanceRespose.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().unit()).isEqualTo(Unit.M);
        
        // Expected distance in meters: approximately 357,000 - 365,000 meters
        BigDecimal distance = response.getBody().distance();
        assertThat(distance).isGreaterThanOrEqualTo(new BigDecimal("357000"));
        assertThat(distance).isLessThanOrEqualTo(new BigDecimal("365000"));
    }

    @Test
    @DisplayName("Should calculate distance in miles")
    void shouldCalculateDistanceInMiles() {
        // São Paulo to Rio de Janeiro in miles
        String url = "/distances?latitudeStart=-23.5505&longitudeStart=-46.6333&latitudeEnd=-22.9068&longitudeEnd=-43.1729&unit=MI";
        
        ResponseEntity<DistanceRespose> response = restTemplate.getForEntity(url, DistanceRespose.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().unit()).isEqualTo(Unit.MI);
        
        // Expected distance in miles: approximately 222-224 miles (357-359 km * 0.62137)
        BigDecimal distance = response.getBody().distance();
        assertThat(distance).isGreaterThanOrEqualTo(new BigDecimal("220"));
        assertThat(distance).isLessThanOrEqualTo(new BigDecimal("225"));
    }

    @Test
    @DisplayName("Should use KM as default unit when unit parameter is not provided")
    void shouldUseKMAsDefaultUnit() {
        String url = "/distances?latitudeStart=-23.5505&longitudeStart=-46.6333&latitudeEnd=-22.9068&longitudeEnd=-43.1729";
        
        ResponseEntity<DistanceRespose> response = restTemplate.getForEntity(url, DistanceRespose.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().unit()).isEqualTo(Unit.KM);
    }

    @Test
    @DisplayName("Should return 400 Bad Request when latitudeStart parameter is missing")
    void shouldReturnBadRequestWhenLatitudeStartIsMissing() {
        String url = "/distances?longitudeStart=-46.6333&latitudeEnd=-22.9068&longitudeEnd=-43.1729";
        
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Should return 400 Bad Request when longitudeStart parameter is missing")
    void shouldReturnBadRequestWhenLongitudeStartIsMissing() {
        String url = "/distances?latitudeStart=-23.5505&latitudeEnd=-22.9068&longitudeEnd=-43.1729";
        
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Should return 400 Bad Request when latitudeEnd parameter is missing")
    void shouldReturnBadRequestWhenLatitudeEndIsMissing() {
        String url = "/distances?latitudeStart=-23.5505&longitudeStart=-46.6333&longitudeEnd=-43.1729";
        
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Should return 400 Bad Request when longitudeEnd parameter is missing")
    void shouldReturnBadRequestWhenLongitudeEndIsMissing() {
        String url = "/distances?latitudeStart=-23.5505&longitudeStart=-46.6333&latitudeEnd=-22.9068";
        
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Should return 400 Bad Request when latitude parameter has invalid format")
    void shouldReturnBadRequestWhenLatitudeHasInvalidFormat() {
        String url = "/distances?latitudeStart=invalid&longitudeStart=-46.6333&latitudeEnd=-22.9068&longitudeEnd=-43.1729";
        
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Should return 400 Bad Request when longitude parameter has invalid format")
    void shouldReturnBadRequestWhenLongitudeHasInvalidFormat() {
        String url = "/distances?latitudeStart=-23.5505&longitudeStart=invalid&latitudeEnd=-22.9068&longitudeEnd=-43.1729";
        
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Should calculate distance with valid latitude at boundary (90 degrees)")
    void shouldCalculateDistanceWithValidLatitudeAtNorthPole() {
        // North Pole (90, 0) to South Pole (-90, 0)
        String url = "/distances?latitudeStart=90&longitudeStart=0&latitudeEnd=-90&longitudeEnd=0&unit=KM";
        
        ResponseEntity<DistanceRespose> response = restTemplate.getForEntity(url, DistanceRespose.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        // Distance from North Pole to South Pole is approximately half the Earth's circumference
        // Earth's circumference = 2 * π * 6378 km ≈ 40,074 km
        // Half circumference ≈ 20,037 km
        BigDecimal distance = response.getBody().distance();
        assertThat(distance).isGreaterThanOrEqualTo(new BigDecimal("20000"));
        assertThat(distance).isLessThanOrEqualTo(new BigDecimal("20100"));
    }

    @Test
    @DisplayName("Should calculate distance with valid longitude at boundary (180 degrees)")
    void shouldCalculateDistanceWithValidLongitudeAtBoundary() {
        // Test with longitude at -180 and 180 (same meridian)
        String url = "/distances?latitudeStart=0&longitudeStart=-180&latitudeEnd=0&longitudeEnd=180&unit=KM";
        
        ResponseEntity<DistanceRespose> response = restTemplate.getForEntity(url, DistanceRespose.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        // Distance should be approximately zero since -180 and 180 represent the same longitude
        BigDecimal distance = response.getBody().distance();
        assertThat(distance).isLessThanOrEqualTo(new BigDecimal("10"));
    }

    @Test
    @DisplayName("Should calculate distance across the equator")
    void shouldCalculateDistanceAcrossEquator() {
        // From Ecuador (0, -78.5) to Brazil (0, -47.9)
        String url = "/distances?latitudeStart=0&longitudeStart=-78.5&latitudeEnd=0&longitudeEnd=-47.9&unit=KM";
        
        ResponseEntity<DistanceRespose> response = restTemplate.getForEntity(url, DistanceRespose.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        // Distance along the equator for ~30.6 degrees of longitude
        // At equator, 1 degree ≈ 111 km, so 30.6 degrees ≈ 3,397 km
        BigDecimal distance = response.getBody().distance();
        assertThat(distance).isGreaterThanOrEqualTo(new BigDecimal("3300"));
        assertThat(distance).isLessThanOrEqualTo(new BigDecimal("3450"));
    }
}
