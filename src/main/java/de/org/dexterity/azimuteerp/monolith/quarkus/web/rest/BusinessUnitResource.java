package de.org.dexterity.azimuteerp.monolith.quarkus.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.BusinessUnitService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.BusinessUnitDTO;
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
 * REST controller for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.BusinessUnit}.
 */
@Path("/api/business-units")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class BusinessUnitResource {

    private final Logger log = LoggerFactory.getLogger(BusinessUnitResource.class);

    private static final String ENTITY_NAME = "businessUnit";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    BusinessUnitService businessUnitService;

    /**
     * {@code POST  /business-units} : Create a new businessUnit.
     *
     * @param businessUnitDTO the businessUnitDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new businessUnitDTO, or with status {@code 400 (Bad Request)} if the businessUnit has already an ID.
     */
    @POST
    public Response createBusinessUnit(@Valid BusinessUnitDTO businessUnitDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save BusinessUnit : {}", businessUnitDTO);
        if (businessUnitDTO.id != null) {
            throw new BadRequestAlertException("A new businessUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = businessUnitService.persistOrUpdate(businessUnitDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /business-units} : Updates an existing businessUnit.
     *
     * @param businessUnitDTO the businessUnitDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated businessUnitDTO,
     * or with status {@code 400 (Bad Request)} if the businessUnitDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the businessUnitDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateBusinessUnit(@Valid BusinessUnitDTO businessUnitDTO, @PathParam("id") Long id) {
        log.debug("REST request to update BusinessUnit : {}", businessUnitDTO);
        if (businessUnitDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = businessUnitService.persistOrUpdate(businessUnitDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessUnitDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /business-units/:id} : delete the "id" businessUnit.
     *
     * @param id the id of the businessUnitDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteBusinessUnit(@PathParam("id") Long id) {
        log.debug("REST request to delete BusinessUnit : {}", id);
        businessUnitService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /business-units} : get all the businessUnits.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of businessUnits in body.
     */
    @GET
    public Response getAllBusinessUnits(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo
    ) {
        log.debug("REST request to get a page of BusinessUnits");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<BusinessUnitDTO> result = businessUnitService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /business-units/:id} : get the "id" businessUnit.
     *
     * @param id the id of the businessUnitDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the businessUnitDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getBusinessUnit(@PathParam("id") Long id) {
        log.debug("REST request to get BusinessUnit : {}", id);
        Optional<BusinessUnitDTO> businessUnitDTO = businessUnitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(businessUnitDTO);
    }
}
