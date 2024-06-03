package de.org.dexterity.azimuteerp.monolith.quarkus.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.WarehouseService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.WarehouseDTO;
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
 * REST controller for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.Warehouse}.
 */
@Path("/api/warehouses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class WarehouseResource {

    private final Logger log = LoggerFactory.getLogger(WarehouseResource.class);

    private static final String ENTITY_NAME = "warehouse";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    WarehouseService warehouseService;

    /**
     * {@code POST  /warehouses} : Create a new warehouse.
     *
     * @param warehouseDTO the warehouseDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new warehouseDTO, or with status {@code 400 (Bad Request)} if the warehouse has already an ID.
     */
    @POST
    public Response createWarehouse(@Valid WarehouseDTO warehouseDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save Warehouse : {}", warehouseDTO);
        if (warehouseDTO.id != null) {
            throw new BadRequestAlertException("A new warehouse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = warehouseService.persistOrUpdate(warehouseDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /warehouses} : Updates an existing warehouse.
     *
     * @param warehouseDTO the warehouseDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated warehouseDTO,
     * or with status {@code 400 (Bad Request)} if the warehouseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the warehouseDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateWarehouse(@Valid WarehouseDTO warehouseDTO, @PathParam("id") Long id) {
        log.debug("REST request to update Warehouse : {}", warehouseDTO);
        if (warehouseDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = warehouseService.persistOrUpdate(warehouseDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, warehouseDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /warehouses/:id} : delete the "id" warehouse.
     *
     * @param id the id of the warehouseDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteWarehouse(@PathParam("id") Long id) {
        log.debug("REST request to delete Warehouse : {}", id);
        warehouseService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /warehouses} : get all the warehouses.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of warehouses in body.
     */
    @GET
    public Response getAllWarehouses(@BeanParam PageRequestVM pageRequest, @BeanParam SortRequestVM sortRequest, @Context UriInfo uriInfo) {
        log.debug("REST request to get a page of Warehouses");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<WarehouseDTO> result = warehouseService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /warehouses/:id} : get the "id" warehouse.
     *
     * @param id the id of the warehouseDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the warehouseDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getWarehouse(@PathParam("id") Long id) {
        log.debug("REST request to get Warehouse : {}", id);
        Optional<WarehouseDTO> warehouseDTO = warehouseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(warehouseDTO);
    }
}
