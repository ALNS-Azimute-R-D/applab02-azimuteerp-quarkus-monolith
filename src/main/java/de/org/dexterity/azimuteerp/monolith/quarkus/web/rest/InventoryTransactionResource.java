package de.org.dexterity.azimuteerp.monolith.quarkus.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.InventoryTransactionService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.InventoryTransactionDTO;
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
 * REST controller for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.InventoryTransaction}.
 */
@Path("/api/inventory-transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class InventoryTransactionResource {

    private final Logger log = LoggerFactory.getLogger(InventoryTransactionResource.class);

    private static final String ENTITY_NAME = "inventoryTransaction";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    InventoryTransactionService inventoryTransactionService;

    /**
     * {@code POST  /inventory-transactions} : Create a new inventoryTransaction.
     *
     * @param inventoryTransactionDTO the inventoryTransactionDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new inventoryTransactionDTO, or with status {@code 400 (Bad Request)} if the inventoryTransaction has already an ID.
     */
    @POST
    public Response createInventoryTransaction(@Valid InventoryTransactionDTO inventoryTransactionDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save InventoryTransaction : {}", inventoryTransactionDTO);
        if (inventoryTransactionDTO.id != null) {
            throw new BadRequestAlertException("A new inventoryTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = inventoryTransactionService.persistOrUpdate(inventoryTransactionDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /inventory-transactions} : Updates an existing inventoryTransaction.
     *
     * @param inventoryTransactionDTO the inventoryTransactionDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated inventoryTransactionDTO,
     * or with status {@code 400 (Bad Request)} if the inventoryTransactionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inventoryTransactionDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateInventoryTransaction(@Valid InventoryTransactionDTO inventoryTransactionDTO, @PathParam("id") Long id) {
        log.debug("REST request to update InventoryTransaction : {}", inventoryTransactionDTO);
        if (inventoryTransactionDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = inventoryTransactionService.persistOrUpdate(inventoryTransactionDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inventoryTransactionDTO.id.toString()).forEach(
            response::header
        );
        return response.build();
    }

    /**
     * {@code DELETE  /inventory-transactions/:id} : delete the "id" inventoryTransaction.
     *
     * @param id the id of the inventoryTransactionDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteInventoryTransaction(@PathParam("id") Long id) {
        log.debug("REST request to delete InventoryTransaction : {}", id);
        inventoryTransactionService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /inventory-transactions} : get all the inventoryTransactions.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of inventoryTransactions in body.
     */
    @GET
    public Response getAllInventoryTransactions(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo
    ) {
        log.debug("REST request to get a page of InventoryTransactions");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<InventoryTransactionDTO> result = inventoryTransactionService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /inventory-transactions/:id} : get the "id" inventoryTransaction.
     *
     * @param id the id of the inventoryTransactionDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the inventoryTransactionDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getInventoryTransaction(@PathParam("id") Long id) {
        log.debug("REST request to get InventoryTransaction : {}", id);
        Optional<InventoryTransactionDTO> inventoryTransactionDTO = inventoryTransactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inventoryTransactionDTO);
    }
}
