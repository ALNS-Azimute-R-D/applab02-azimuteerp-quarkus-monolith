package org.dexterity.darueira.azimuteerp.monolith.quarkus.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.OrganizationMemberRoleService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.OrganizationMemberRoleDTO;
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
 * REST controller for managing {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.OrganizationMemberRole}.
 */
@Path("/api/organization-member-roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class OrganizationMemberRoleResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationMemberRoleResource.class);

    private static final String ENTITY_NAME = "organizationMemberRole";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    OrganizationMemberRoleService organizationMemberRoleService;

    /**
     * {@code POST  /organization-member-roles} : Create a new organizationMemberRole.
     *
     * @param organizationMemberRoleDTO the organizationMemberRoleDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new organizationMemberRoleDTO, or with status {@code 400 (Bad Request)} if the organizationMemberRole has already an ID.
     */
    @POST
    public Response createOrganizationMemberRole(@Valid OrganizationMemberRoleDTO organizationMemberRoleDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save OrganizationMemberRole : {}", organizationMemberRoleDTO);
        if (organizationMemberRoleDTO.id != null) {
            throw new BadRequestAlertException("A new organizationMemberRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = organizationMemberRoleService.persistOrUpdate(organizationMemberRoleDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /organization-member-roles} : Updates an existing organizationMemberRole.
     *
     * @param organizationMemberRoleDTO the organizationMemberRoleDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated organizationMemberRoleDTO,
     * or with status {@code 400 (Bad Request)} if the organizationMemberRoleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organizationMemberRoleDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateOrganizationMemberRole(@Valid OrganizationMemberRoleDTO organizationMemberRoleDTO, @PathParam("id") Long id) {
        log.debug("REST request to update OrganizationMemberRole : {}", organizationMemberRoleDTO);
        if (organizationMemberRoleDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = organizationMemberRoleService.persistOrUpdate(organizationMemberRoleDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organizationMemberRoleDTO.id.toString()).forEach(
            response::header
        );
        return response.build();
    }

    /**
     * {@code DELETE  /organization-member-roles/:id} : delete the "id" organizationMemberRole.
     *
     * @param id the id of the organizationMemberRoleDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteOrganizationMemberRole(@PathParam("id") Long id) {
        log.debug("REST request to delete OrganizationMemberRole : {}", id);
        organizationMemberRoleService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /organization-member-roles} : get all the organizationMemberRoles.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of organizationMemberRoles in body.
     */
    @GET
    public Response getAllOrganizationMemberRoles(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo
    ) {
        log.debug("REST request to get a page of OrganizationMemberRoles");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<OrganizationMemberRoleDTO> result = organizationMemberRoleService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /organization-member-roles/:id} : get the "id" organizationMemberRole.
     *
     * @param id the id of the organizationMemberRoleDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the organizationMemberRoleDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getOrganizationMemberRole(@PathParam("id") Long id) {
        log.debug("REST request to get OrganizationMemberRole : {}", id);
        Optional<OrganizationMemberRoleDTO> organizationMemberRoleDTO = organizationMemberRoleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organizationMemberRoleDTO);
    }
}
