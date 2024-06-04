package de.org.dexterity.azimuteerp.monolith.quarkus.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.AssetService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.AssetDTO;
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
 * REST controller for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.Asset}.
 */
@Path("/api/assets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class AssetResource {

    private final Logger log = LoggerFactory.getLogger(AssetResource.class);

    private static final String ENTITY_NAME = "asset";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    AssetService assetService;

    /**
     * {@code POST  /assets} : Create a new asset.
     *
     * @param assetDTO the assetDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new assetDTO, or with status {@code 400 (Bad Request)} if the asset has already an ID.
     */
    @POST
    public Response createAsset(@Valid AssetDTO assetDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save Asset : {}", assetDTO);
        if (assetDTO.id != null) {
            throw new BadRequestAlertException("A new asset cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = assetService.persistOrUpdate(assetDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /assets} : Updates an existing asset.
     *
     * @param assetDTO the assetDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated assetDTO,
     * or with status {@code 400 (Bad Request)} if the assetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateAsset(@Valid AssetDTO assetDTO, @PathParam("id") Long id) {
        log.debug("REST request to update Asset : {}", assetDTO);
        if (assetDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = assetService.persistOrUpdate(assetDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /assets/:id} : delete the "id" asset.
     *
     * @param id the id of the assetDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteAsset(@PathParam("id") Long id) {
        log.debug("REST request to delete Asset : {}", id);
        assetService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /assets} : get all the assets.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of assets in body.
     */
    @GET
    public Response getAllAssets(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo,
        @QueryParam("filter") String filter
    ) {
        log.debug("REST request to get a page of Assets");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<AssetDTO> result = assetService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /assets/:id} : get the "id" asset.
     *
     * @param id the id of the assetDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the assetDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getAsset(@PathParam("id") Long id) {
        log.debug("REST request to get Asset : {}", id);
        Optional<AssetDTO> assetDTO = assetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetDTO);
    }
}
