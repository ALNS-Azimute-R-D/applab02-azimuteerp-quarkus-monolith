package org.dexterity.darueira.azimuteerp.monolith.quarkus.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.AssetCollectionService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.AssetCollectionDTO;
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
 * REST controller for managing {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.AssetCollection}.
 */
@Path("/api/asset-collections")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class AssetCollectionResource {

    private final Logger log = LoggerFactory.getLogger(AssetCollectionResource.class);

    private static final String ENTITY_NAME = "assetCollection";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    AssetCollectionService assetCollectionService;

    /**
     * {@code POST  /asset-collections} : Create a new assetCollection.
     *
     * @param assetCollectionDTO the assetCollectionDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new assetCollectionDTO, or with status {@code 400 (Bad Request)} if the assetCollection has already an ID.
     */
    @POST
    public Response createAssetCollection(@Valid AssetCollectionDTO assetCollectionDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save AssetCollection : {}", assetCollectionDTO);
        if (assetCollectionDTO.id != null) {
            throw new BadRequestAlertException("A new assetCollection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = assetCollectionService.persistOrUpdate(assetCollectionDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /asset-collections} : Updates an existing assetCollection.
     *
     * @param assetCollectionDTO the assetCollectionDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated assetCollectionDTO,
     * or with status {@code 400 (Bad Request)} if the assetCollectionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetCollectionDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateAssetCollection(@Valid AssetCollectionDTO assetCollectionDTO, @PathParam("id") Long id) {
        log.debug("REST request to update AssetCollection : {}", assetCollectionDTO);
        if (assetCollectionDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = assetCollectionService.persistOrUpdate(assetCollectionDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetCollectionDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /asset-collections/:id} : delete the "id" assetCollection.
     *
     * @param id the id of the assetCollectionDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteAssetCollection(@PathParam("id") Long id) {
        log.debug("REST request to delete AssetCollection : {}", id);
        assetCollectionService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /asset-collections} : get all the assetCollections.
     *
     * @param pageRequest the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link Response} with status {@code 200 (OK)} and the list of assetCollections in body.
     */
    @GET
    public Response getAllAssetCollections(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo,
        @QueryParam(value = "eagerload") boolean eagerload
    ) {
        log.debug("REST request to get a page of AssetCollections");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<AssetCollectionDTO> result;
        if (eagerload) {
            result = assetCollectionService.findAllWithEagerRelationships(page);
        } else {
            result = assetCollectionService.findAll(page);
        }
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /asset-collections/:id} : get the "id" assetCollection.
     *
     * @param id the id of the assetCollectionDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the assetCollectionDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getAssetCollection(@PathParam("id") Long id) {
        log.debug("REST request to get AssetCollection : {}", id);
        Optional<AssetCollectionDTO> assetCollectionDTO = assetCollectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetCollectionDTO);
    }
}
