package com.payment.Payment.Controller.v1;

import com.payment.Payment.DTOs.Request.PageableRequestDto;
import com.payment.Payment.DTOs.Request.UnitPriceRequest;
import com.payment.Payment.DTOs.Respone.PageableResponseDto;
import com.payment.Payment.DTOs.Respone.ResponseObject;
import com.payment.Payment.DTOs.Respone.UnitPriceResponse;
import com.payment.Payment.Enum.TableType;
import com.payment.Payment.Service.UnitPriceService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Tag(name = "Unit Price V1", description = "Unit Price API")
@RestController
@RequestMapping("v1/prices")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UnitPriceV1Controller {

    private final UnitPriceService unitPriceService;

    @PostMapping
    public ResponseObject createUnitPrice(@RequestBody UnitPriceRequest unitPriceRequest) {
        return ResponseObject.builder()
                .status(1000)
                .message("Unit Price created successfully")
                .data(unitPriceService.createUnitPrice(unitPriceRequest))
                .build();
    }

    @GetMapping
    public ResponseObject getUnitPrices(
            @Parameter(description = "Query type: all, byStore",
                    required = true,
                    schema = @Schema(
                            allowableValues = {"all", "byType", "byStore"}
                    ))
            @RequestParam(required = true, defaultValue = "all") String queryType,

            @Parameter(description = "Store ID (required for queryType=byStore)")
            @RequestParam(required = false) String storeId,

            @Parameter(description = "Table Type",
                    required = false,
                    schema = @Schema(
                            allowableValues = {"null", "Pool", "Carom", "Snooker"}
                    ))
            @RequestParam(required = false, defaultValue = "null") String tableType,

            @Parameter(description = "Page number (1-based)", required = true)
            @RequestParam(required = false, defaultValue = "1") Integer page,

            @Parameter(description = "Page size", required = true)
            @RequestParam(required = false, defaultValue = "10") Integer size,

            @Parameter(description = "Sort By",
                    required = true,
                    schema = @Schema(
                            allowableValues = {"unitPriceID", "storeID"}
                    ))
            @RequestParam(defaultValue = "unitPriceID") String sortBy,

            @Parameter(description = "Sort Direction",
                    required = true,
                    schema = @Schema(
                            allowableValues = {"desc", "asc"}
                    ))
            @RequestParam(required = true, defaultValue = "desc") String sortDirection
    ) {

        PageableRequestDto req = PageableRequestDto.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        Map<String, Object> filters = new HashMap<>();
        filters.put("queryType", queryType);
        if (storeId != null && !storeId.isEmpty()) filters.put("storeId", storeId);
        if ( tableType!= null && !"null".equals(tableType)) filters.put("tableType", tableType);

        PageableResponseDto<UnitPriceResponse> p = unitPriceService.getAll(req, filters);

        return ResponseObject.builder()
                .status(1000)
                .message("Data")
                .data(p)
                .build();
    }

    @PutMapping
    public ResponseObject updateUnitPrice(@RequestBody UnitPriceRequest unitPriceRequest) {
        return ResponseObject.builder()
                .status(1000)
                .message("Unit Price updated successfully")
                .data(unitPriceService.updateUnitPrice(unitPriceRequest))
                .build();
    }

    @DeleteMapping
    public ResponseObject deleteUnitPrice(@RequestParam Integer unitPriceID) {
        return ResponseObject.builder()
                .status(1000)
                .message("Unit Price deleted successfully")
                .data(unitPriceService.deleteUnitPrice(unitPriceID))
                .build();
    }

}
