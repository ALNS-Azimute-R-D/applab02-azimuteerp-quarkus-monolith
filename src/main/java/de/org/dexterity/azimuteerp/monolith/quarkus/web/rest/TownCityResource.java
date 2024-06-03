package de.org.dexterity.azimuteerp.monolith.quarkus.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.TownCityService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.TownCityDTO;
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
 * REST controller for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.TownCity}.
 */
@Path("/api/town-cities")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class TownCityResource {

    private final Logger log = LoggerFactory.getLogger(TownCityResource.class);

    private static final String ENTITY_NAME = "townCity";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    TownCityService townCityService;

    /**
     * {@code POST  /town-cities} : Create a new townCity.
     *
     * @param townCityDTO the townCityDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new townCityDTO, or with status {@code 400 (Bad Request)} if the townCity has already an ID.
     */
    @POST
    public Response createTownCity(@Valid TownCityDTO townCityDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save TownCity : {}", townCityDTO);
        if (townCityDTO.id != null) {
            throw new BadRequestAlertException("A new townCity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = townCityService.persistOrUpdate(townCityDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /town-cities} : Updates an existing townCity.
     *
     * @param townCityDTO the townCityDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated townCityDTO,
     * or with status {@code 400 (Bad Request)} if the townCityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the townCityDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateTownCity(@Valid TownCityDTO townCityDTO, @PathParam("id") Long id) {
        log.debug("REST request to update TownCity : {}", townCityDTO);
        if (townCityDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = townCityService.persistOrUpdate(townCityDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, townCityDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /town-cities/:id} : delete the "id" townCity.
     *
     * @param id the id of the townCityDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteTownCity(@PathParam("id") Long id) {
        log.debug("REST request to delete TownCity : {}", id);
        townCityService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /town-cities} : get all the townCities.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of townCities in body.
     */
    @GET
    public Response getAllTownCities(@BeanParam PageRequestVM pageRequest, @BeanParam SortRequestVM sortRequest, @Context UriInfo uriInfo) {
        log.debug("REST request to get a page of TownCities");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<TownCityDTO> result = townCityService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /town-cities/:id} : get the "id" townCity.
     *
     * @param id the id of the townCityDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the townCityDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getTownCity(@PathParam("id") Long id) {
        log.debug("REST request to get TownCity : {}", id);
        Optional<TownCityDTO> townCityDTO = townCityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(townCityDTO);
    }
}
