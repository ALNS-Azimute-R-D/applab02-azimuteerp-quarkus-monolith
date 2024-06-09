package org.dexterity.darueira.azimuteerp.monolith.quarkus.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.AssetMetadataService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.AssetMetadataDTO;
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
 * REST controller for managing {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.AssetMetadata}.
 */
@Path("/api/asset-metadata")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class AssetMetadataResource {

    private final Logger log = LoggerFactory.getLogger(AssetMetadataResource.class);

    private static final String ENTITY_NAME = "assetMetadata";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    AssetMetadataService assetMetadataService;

    /**
     * {@code POST  /asset-metadata} : Create a new assetMetadata.
     *
     * @param assetMetadataDTO the assetMetadataDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new assetMetadataDTO, or with status {@code 400 (Bad Request)} if the assetMetadata has already an ID.
     */
    @POST
    public Response createAssetMetadata(@Valid AssetMetadataDTO assetMetadataDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save AssetMetadata : {}", assetMetadataDTO);
        if (assetMetadataDTO.id != null) {
            throw new BadRequestAlertException("A new assetMetadata cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = assetMetadataService.persistOrUpdate(assetMetadataDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /asset-metadata} : Updates an existing assetMetadata.
     *
     * @param assetMetadataDTO the assetMetadataDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated assetMetadataDTO,
     * or with status {@code 400 (Bad Request)} if the assetMetadataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetMetadataDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateAssetMetadata(@Valid AssetMetadataDTO assetMetadataDTO, @PathParam("id") Long id) {
        log.debug("REST request to update AssetMetadata : {}", assetMetadataDTO);
        if (assetMetadataDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = assetMetadataService.persistOrUpdate(assetMetadataDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetMetadataDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /asset-metadata/:id} : delete the "id" assetMetadata.
     *
     * @param id the id of the assetMetadataDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteAssetMetadata(@PathParam("id") Long id) {
        log.debug("REST request to delete AssetMetadata : {}", id);
        assetMetadataService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /asset-metadata} : get all the assetMetadata.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of assetMetadata in body.
     */
    @GET
    public Response getAllAssetMetadata(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo
    ) {
        log.debug("REST request to get a page of AssetMetadata");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<AssetMetadataDTO> result = assetMetadataService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /asset-metadata/:id} : get the "id" assetMetadata.
     *
     * @param id the id of the assetMetadataDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the assetMetadataDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getAssetMetadata(@PathParam("id") Long id) {
        log.debug("REST request to get AssetMetadata : {}", id);
        Optional<AssetMetadataDTO> assetMetadataDTO = assetMetadataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetMetadataDTO);
    }
}
