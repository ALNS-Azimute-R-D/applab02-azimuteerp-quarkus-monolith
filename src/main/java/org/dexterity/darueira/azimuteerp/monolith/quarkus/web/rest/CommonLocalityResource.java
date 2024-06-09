package org.dexterity.darueira.azimuteerp.monolith.quarkus.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.CommonLocalityService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.CommonLocalityDTO;
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
 * REST controller for managing {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.CommonLocality}.
 */
@Path("/api/common-localities")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class CommonLocalityResource {

    private final Logger log = LoggerFactory.getLogger(CommonLocalityResource.class);

    private static final String ENTITY_NAME = "commonLocality";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    CommonLocalityService commonLocalityService;

    /**
     * {@code POST  /common-localities} : Create a new commonLocality.
     *
     * @param commonLocalityDTO the commonLocalityDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new commonLocalityDTO, or with status {@code 400 (Bad Request)} if the commonLocality has already an ID.
     */
    @POST
    public Response createCommonLocality(@Valid CommonLocalityDTO commonLocalityDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save CommonLocality : {}", commonLocalityDTO);
        if (commonLocalityDTO.id != null) {
            throw new BadRequestAlertException("A new commonLocality cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = commonLocalityService.persistOrUpdate(commonLocalityDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /common-localities} : Updates an existing commonLocality.
     *
     * @param commonLocalityDTO the commonLocalityDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated commonLocalityDTO,
     * or with status {@code 400 (Bad Request)} if the commonLocalityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commonLocalityDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateCommonLocality(@Valid CommonLocalityDTO commonLocalityDTO, @PathParam("id") Long id) {
        log.debug("REST request to update CommonLocality : {}", commonLocalityDTO);
        if (commonLocalityDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = commonLocalityService.persistOrUpdate(commonLocalityDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commonLocalityDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /common-localities/:id} : delete the "id" commonLocality.
     *
     * @param id the id of the commonLocalityDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteCommonLocality(@PathParam("id") Long id) {
        log.debug("REST request to delete CommonLocality : {}", id);
        commonLocalityService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /common-localities} : get all the commonLocalities.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of commonLocalities in body.
     */
    @GET
    public Response getAllCommonLocalities(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo
    ) {
        log.debug("REST request to get a page of CommonLocalities");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<CommonLocalityDTO> result = commonLocalityService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /common-localities/:id} : get the "id" commonLocality.
     *
     * @param id the id of the commonLocalityDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the commonLocalityDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getCommonLocality(@PathParam("id") Long id) {
        log.debug("REST request to get CommonLocality : {}", id);
        Optional<CommonLocalityDTO> commonLocalityDTO = commonLocalityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commonLocalityDTO);
    }
}
