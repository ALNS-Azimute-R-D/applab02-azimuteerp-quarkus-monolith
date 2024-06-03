package de.org.dexterity.azimuteerp.monolith.quarkus.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.StockLevelService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.StockLevelDTO;
import de.org.dexterity.azimuteerp.monolith.quarkus.web.rest.errors.BadRequestAlertException;
import de.org.dexterity.azimuteerp.monolith.quarkus.web.rest.vm.PageRequestVM;
import de.org.dexterity.azimuteerp.monolith.quarkus.web.rest.vm.SortRequestVM;
import de.org.dexterity.azimuteerp.monolith.quarkus.web.util.HeaderUtil;
import de.org.dexterity.azimuteerp.monolith.quarkus.web.util.PaginationUtil;
import de.org.dexterity.azimuteerp.monolith.quarkus.web.util.ResponseUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.Optional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST controller for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.StockLevel}.
 */
@Path("/api/stock-levels")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class StockLevelResource {

    private final Logger log = LoggerFactory.getLogger(StockLevelResource.class);

    private static final String ENTITY_NAME = "stockLevel";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    StockLevelService stockLevelService;

    /**
     * {@code POST  /stock-levels} : Create a new stockLevel.
     *
     * @param stockLevelDTO the stockLevelDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new stockLevelDTO, or with status {@code 400 (Bad Request)} if the stockLevel has already an ID.
     */
    @POST
    public Response createStockLevel(@Valid StockLevelDTO stockLevelDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save StockLevel : {}", stockLevelDTO);
        if (stockLevelDTO.id != null) {
            throw new BadRequestAlertException("A new stockLevel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = stockLevelService.persistOrUpdate(stockLevelDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /stock-levels} : Updates an existing stockLevel.
     *
     * @param stockLevelDTO the stockLevelDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated stockLevelDTO,
     * or with status {@code 400 (Bad Request)} if the stockLevelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stockLevelDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateStockLevel(@Valid StockLevelDTO stockLevelDTO, @PathParam("id") Long id) {
        log.debug("REST request to update StockLevel : {}", stockLevelDTO);
        if (stockLevelDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = stockLevelService.persistOrUpdate(stockLevelDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stockLevelDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /stock-levels/:id} : delete the "id" stockLevel.
     *
     * @param id the id of the stockLevelDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteStockLevel(@PathParam("id") Long id) {
        log.debug("REST request to delete StockLevel : {}", id);
        stockLevelService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /stock-levels} : get all the stockLevels.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of stockLevels in body.
     */
    @GET
    public Response getAllStockLevels(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo
    ) {
        log.debug("REST request to get a page of StockLevels");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<StockLevelDTO> result = stockLevelService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /stock-levels/:id} : get the "id" stockLevel.
     *
     * @param id the id of the stockLevelDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the stockLevelDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getStockLevel(@PathParam("id") Long id) {
        log.debug("REST request to get StockLevel : {}", id);
        Optional<StockLevelDTO> stockLevelDTO = stockLevelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockLevelDTO);
    }
}
