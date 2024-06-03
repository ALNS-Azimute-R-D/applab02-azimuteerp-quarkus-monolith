package de.org.dexterity.azimuteerp.monolith.quarkus.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.LocalityService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.LocalityDTO;
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
 * REST controller for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.Locality}.
 */
@Path("/api/localities")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class LocalityResource {

    private final Logger log = LoggerFactory.getLogger(LocalityResource.class);

    private static final String ENTITY_NAME = "locality";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    LocalityService localityService;

    /**
     * {@code POST  /localities} : Create a new locality.
     *
     * @param localityDTO the localityDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new localityDTO, or with status {@code 400 (Bad Request)} if the locality has already an ID.
     */
    @POST
    public Response createLocality(@Valid LocalityDTO localityDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save Locality : {}", localityDTO);
        if (localityDTO.id != null) {
            throw new BadRequestAlertException("A new locality cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = localityService.persistOrUpdate(localityDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /localities} : Updates an existing locality.
     *
     * @param localityDTO the localityDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated localityDTO,
     * or with status {@code 400 (Bad Request)} if the localityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the localityDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateLocality(@Valid LocalityDTO localityDTO, @PathParam("id") Long id) {
        log.debug("REST request to update Locality : {}", localityDTO);
        if (localityDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = localityService.persistOrUpdate(localityDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, localityDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /localities/:id} : delete the "id" locality.
     *
     * @param id the id of the localityDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteLocality(@PathParam("id") Long id) {
        log.debug("REST request to delete Locality : {}", id);
        localityService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /localities} : get all the localities.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of localities in body.
     */
    @GET
    public Response getAllLocalities(@BeanParam PageRequestVM pageRequest, @BeanParam SortRequestVM sortRequest, @Context UriInfo uriInfo) {
        log.debug("REST request to get a page of Localities");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<LocalityDTO> result = localityService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /localities/:id} : get the "id" locality.
     *
     * @param id the id of the localityDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the localityDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getLocality(@PathParam("id") Long id) {
        log.debug("REST request to get Locality : {}", id);
        Optional<LocalityDTO> localityDTO = localityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(localityDTO);
    }
}
