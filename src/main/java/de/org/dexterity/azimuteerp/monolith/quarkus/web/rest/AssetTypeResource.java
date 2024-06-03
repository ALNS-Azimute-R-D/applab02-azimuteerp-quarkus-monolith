package de.org.dexterity.azimuteerp.monolith.quarkus.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.AssetTypeService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.AssetTypeDTO;
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
 * REST controller for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.AssetType}.
 */
@Path("/api/asset-types")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class AssetTypeResource {

    private final Logger log = LoggerFactory.getLogger(AssetTypeResource.class);

    private static final String ENTITY_NAME = "assetType";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    AssetTypeService assetTypeService;

    /**
     * {@code POST  /asset-types} : Create a new assetType.
     *
     * @param assetTypeDTO the assetTypeDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new assetTypeDTO, or with status {@code 400 (Bad Request)} if the assetType has already an ID.
     */
    @POST
    public Response createAssetType(@Valid AssetTypeDTO assetTypeDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save AssetType : {}", assetTypeDTO);
        if (assetTypeDTO.id != null) {
            throw new BadRequestAlertException("A new assetType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = assetTypeService.persistOrUpdate(assetTypeDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /asset-types} : Updates an existing assetType.
     *
     * @param assetTypeDTO the assetTypeDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated assetTypeDTO,
     * or with status {@code 400 (Bad Request)} if the assetTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetTypeDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateAssetType(@Valid AssetTypeDTO assetTypeDTO, @PathParam("id") Long id) {
        log.debug("REST request to update AssetType : {}", assetTypeDTO);
        if (assetTypeDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = assetTypeService.persistOrUpdate(assetTypeDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetTypeDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /asset-types/:id} : delete the "id" assetType.
     *
     * @param id the id of the assetTypeDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteAssetType(@PathParam("id") Long id) {
        log.debug("REST request to delete AssetType : {}", id);
        assetTypeService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /asset-types} : get all the assetTypes.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of assetTypes in body.
     */
    @GET
    public Response getAllAssetTypes(@BeanParam PageRequestVM pageRequest, @BeanParam SortRequestVM sortRequest, @Context UriInfo uriInfo) {
        log.debug("REST request to get a page of AssetTypes");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<AssetTypeDTO> result = assetTypeService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /asset-types/:id} : get the "id" assetType.
     *
     * @param id the id of the assetTypeDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the assetTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getAssetType(@PathParam("id") Long id) {
        log.debug("REST request to get AssetType : {}", id);
        Optional<AssetTypeDTO> assetTypeDTO = assetTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetTypeDTO);
    }
}
