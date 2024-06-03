package de.org.dexterity.azimuteerp.monolith.quarkus.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.RawAssetProcTmpService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.RawAssetProcTmpDTO;
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
 * REST controller for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.RawAssetProcTmp}.
 */
@Path("/api/raw-asset-proc-tmps")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class RawAssetProcTmpResource {

    private final Logger log = LoggerFactory.getLogger(RawAssetProcTmpResource.class);

    private static final String ENTITY_NAME = "rawAssetProcTmp";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    RawAssetProcTmpService rawAssetProcTmpService;

    /**
     * {@code POST  /raw-asset-proc-tmps} : Create a new rawAssetProcTmp.
     *
     * @param rawAssetProcTmpDTO the rawAssetProcTmpDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new rawAssetProcTmpDTO, or with status {@code 400 (Bad Request)} if the rawAssetProcTmp has already an ID.
     */
    @POST
    public Response createRawAssetProcTmp(@Valid RawAssetProcTmpDTO rawAssetProcTmpDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save RawAssetProcTmp : {}", rawAssetProcTmpDTO);
        if (rawAssetProcTmpDTO.id != null) {
            throw new BadRequestAlertException("A new rawAssetProcTmp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = rawAssetProcTmpService.persistOrUpdate(rawAssetProcTmpDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /raw-asset-proc-tmps} : Updates an existing rawAssetProcTmp.
     *
     * @param rawAssetProcTmpDTO the rawAssetProcTmpDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated rawAssetProcTmpDTO,
     * or with status {@code 400 (Bad Request)} if the rawAssetProcTmpDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rawAssetProcTmpDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateRawAssetProcTmp(@Valid RawAssetProcTmpDTO rawAssetProcTmpDTO, @PathParam("id") Long id) {
        log.debug("REST request to update RawAssetProcTmp : {}", rawAssetProcTmpDTO);
        if (rawAssetProcTmpDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = rawAssetProcTmpService.persistOrUpdate(rawAssetProcTmpDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rawAssetProcTmpDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /raw-asset-proc-tmps/:id} : delete the "id" rawAssetProcTmp.
     *
     * @param id the id of the rawAssetProcTmpDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteRawAssetProcTmp(@PathParam("id") Long id) {
        log.debug("REST request to delete RawAssetProcTmp : {}", id);
        rawAssetProcTmpService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /raw-asset-proc-tmps} : get all the rawAssetProcTmps.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of rawAssetProcTmps in body.
     */
    @GET
    public Response getAllRawAssetProcTmps(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo
    ) {
        log.debug("REST request to get a page of RawAssetProcTmps");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<RawAssetProcTmpDTO> result = rawAssetProcTmpService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /raw-asset-proc-tmps/:id} : get the "id" rawAssetProcTmp.
     *
     * @param id the id of the rawAssetProcTmpDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the rawAssetProcTmpDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getRawAssetProcTmp(@PathParam("id") Long id) {
        log.debug("REST request to get RawAssetProcTmp : {}", id);
        Optional<RawAssetProcTmpDTO> rawAssetProcTmpDTO = rawAssetProcTmpService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rawAssetProcTmpDTO);
    }
}
