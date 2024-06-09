package org.dexterity.darueira.azimuteerp.monolith.quarkus.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.SupplierService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.SupplierDTO;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.web.rest.errors.BadRequestAlertException;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.web.rest.vm.PageRequestVM;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.web.rest.vm.SortRequestVM;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.web.util.HeaderUtil;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.web.util.PaginationUtil;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.web.util.ResponseUtil;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST controller for managing {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Supplier}.
 */
@Path("/api/suppliers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class SupplierResource {

    private final Logger log = LoggerFactory.getLogger(SupplierResource.class);

    private static final String ENTITY_NAME = "supplier";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    SupplierService supplierService;

    /**
     * {@code POST  /suppliers} : Create a new supplier.
     *
     * @param supplierDTO the supplierDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new supplierDTO, or with status {@code 400 (Bad Request)} if the supplier has already an ID.
     */
    @POST
    public Response createSupplier(@Valid SupplierDTO supplierDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save Supplier : {}", supplierDTO);
        if (supplierDTO.id != null) {
            throw new BadRequestAlertException("A new supplier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = supplierService.persistOrUpdate(supplierDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /suppliers} : Updates an existing supplier.
     *
     * @param supplierDTO the supplierDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated supplierDTO,
     * or with status {@code 400 (Bad Request)} if the supplierDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplierDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateSupplier(@Valid SupplierDTO supplierDTO, @PathParam("id") Long id) {
        log.debug("REST request to update Supplier : {}", supplierDTO);
        if (supplierDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = supplierService.persistOrUpdate(supplierDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /suppliers/:id} : delete the "id" supplier.
     *
     * @param id the id of the supplierDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteSupplier(@PathParam("id") Long id) {
        log.debug("REST request to delete Supplier : {}", id);
        supplierService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /suppliers} : get all the suppliers.
     *
     * @param pageRequest the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link Response} with status {@code 200 (OK)} and the list of suppliers in body.
     */
    @GET
    public Response getAllSuppliers(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo,
        @QueryParam(value = "eagerload") boolean eagerload
    ) {
        log.debug("REST request to get a page of Suppliers");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<SupplierDTO> result;
        if (eagerload) {
            result = supplierService.findAllWithEagerRelationships(page);
        } else {
            result = supplierService.findAll(page);
        }
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /suppliers/:id} : get the "id" supplier.
     *
     * @param id the id of the supplierDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the supplierDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getSupplier(@PathParam("id") Long id) {
        log.debug("REST request to get Supplier : {}", id);
        Optional<SupplierDTO> supplierDTO = supplierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplierDTO);
    }
}
