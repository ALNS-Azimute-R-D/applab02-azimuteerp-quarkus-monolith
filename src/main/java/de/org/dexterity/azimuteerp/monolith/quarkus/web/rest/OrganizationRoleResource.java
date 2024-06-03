package de.org.dexterity.azimuteerp.monolith.quarkus.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.OrganizationRoleService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.OrganizationRoleDTO;
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
 * REST controller for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationRole}.
 */
@Path("/api/organization-roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class OrganizationRoleResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationRoleResource.class);

    private static final String ENTITY_NAME = "organizationRole";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    OrganizationRoleService organizationRoleService;

    /**
     * {@code POST  /organization-roles} : Create a new organizationRole.
     *
     * @param organizationRoleDTO the organizationRoleDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new organizationRoleDTO, or with status {@code 400 (Bad Request)} if the organizationRole has already an ID.
     */
    @POST
    public Response createOrganizationRole(@Valid OrganizationRoleDTO organizationRoleDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save OrganizationRole : {}", organizationRoleDTO);
        if (organizationRoleDTO.id != null) {
            throw new BadRequestAlertException("A new organizationRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = organizationRoleService.persistOrUpdate(organizationRoleDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /organization-roles} : Updates an existing organizationRole.
     *
     * @param organizationRoleDTO the organizationRoleDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated organizationRoleDTO,
     * or with status {@code 400 (Bad Request)} if the organizationRoleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organizationRoleDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateOrganizationRole(@Valid OrganizationRoleDTO organizationRoleDTO, @PathParam("id") Long id) {
        log.debug("REST request to update OrganizationRole : {}", organizationRoleDTO);
        if (organizationRoleDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = organizationRoleService.persistOrUpdate(organizationRoleDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organizationRoleDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /organization-roles/:id} : delete the "id" organizationRole.
     *
     * @param id the id of the organizationRoleDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteOrganizationRole(@PathParam("id") Long id) {
        log.debug("REST request to delete OrganizationRole : {}", id);
        organizationRoleService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /organization-roles} : get all the organizationRoles.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of organizationRoles in body.
     */
    @GET
    public Response getAllOrganizationRoles(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo
    ) {
        log.debug("REST request to get a page of OrganizationRoles");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<OrganizationRoleDTO> result = organizationRoleService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /organization-roles/:id} : get the "id" organizationRole.
     *
     * @param id the id of the organizationRoleDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the organizationRoleDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getOrganizationRole(@PathParam("id") Long id) {
        log.debug("REST request to get OrganizationRole : {}", id);
        Optional<OrganizationRoleDTO> organizationRoleDTO = organizationRoleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organizationRoleDTO);
    }
}
