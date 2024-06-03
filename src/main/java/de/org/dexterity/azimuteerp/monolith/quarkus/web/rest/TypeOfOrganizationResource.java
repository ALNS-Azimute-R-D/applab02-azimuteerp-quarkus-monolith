package de.org.dexterity.azimuteerp.monolith.quarkus.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.TypeOfOrganizationService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.TypeOfOrganizationDTO;
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
 * REST controller for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.TypeOfOrganization}.
 */
@Path("/api/type-of-organizations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class TypeOfOrganizationResource {

    private final Logger log = LoggerFactory.getLogger(TypeOfOrganizationResource.class);

    private static final String ENTITY_NAME = "typeOfOrganization";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    TypeOfOrganizationService typeOfOrganizationService;

    /**
     * {@code POST  /type-of-organizations} : Create a new typeOfOrganization.
     *
     * @param typeOfOrganizationDTO the typeOfOrganizationDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new typeOfOrganizationDTO, or with status {@code 400 (Bad Request)} if the typeOfOrganization has already an ID.
     */
    @POST
    public Response createTypeOfOrganization(@Valid TypeOfOrganizationDTO typeOfOrganizationDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save TypeOfOrganization : {}", typeOfOrganizationDTO);
        if (typeOfOrganizationDTO.id != null) {
            throw new BadRequestAlertException("A new typeOfOrganization cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = typeOfOrganizationService.persistOrUpdate(typeOfOrganizationDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /type-of-organizations} : Updates an existing typeOfOrganization.
     *
     * @param typeOfOrganizationDTO the typeOfOrganizationDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated typeOfOrganizationDTO,
     * or with status {@code 400 (Bad Request)} if the typeOfOrganizationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeOfOrganizationDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateTypeOfOrganization(@Valid TypeOfOrganizationDTO typeOfOrganizationDTO, @PathParam("id") Long id) {
        log.debug("REST request to update TypeOfOrganization : {}", typeOfOrganizationDTO);
        if (typeOfOrganizationDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = typeOfOrganizationService.persistOrUpdate(typeOfOrganizationDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeOfOrganizationDTO.id.toString()).forEach(
            response::header
        );
        return response.build();
    }

    /**
     * {@code DELETE  /type-of-organizations/:id} : delete the "id" typeOfOrganization.
     *
     * @param id the id of the typeOfOrganizationDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteTypeOfOrganization(@PathParam("id") Long id) {
        log.debug("REST request to delete TypeOfOrganization : {}", id);
        typeOfOrganizationService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /type-of-organizations} : get all the typeOfOrganizations.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of typeOfOrganizations in body.
     */
    @GET
    public Response getAllTypeOfOrganizations(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo
    ) {
        log.debug("REST request to get a page of TypeOfOrganizations");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<TypeOfOrganizationDTO> result = typeOfOrganizationService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /type-of-organizations/:id} : get the "id" typeOfOrganization.
     *
     * @param id the id of the typeOfOrganizationDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the typeOfOrganizationDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getTypeOfOrganization(@PathParam("id") Long id) {
        log.debug("REST request to get TypeOfOrganization : {}", id);
        Optional<TypeOfOrganizationDTO> typeOfOrganizationDTO = typeOfOrganizationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeOfOrganizationDTO);
    }
}
