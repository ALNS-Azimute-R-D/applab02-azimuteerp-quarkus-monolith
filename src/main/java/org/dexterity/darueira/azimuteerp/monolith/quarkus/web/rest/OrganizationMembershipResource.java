package org.dexterity.darueira.azimuteerp.monolith.quarkus.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.OrganizationMembershipService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.OrganizationMembershipDTO;
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
 * REST controller for managing {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.OrganizationMembership}.
 */
@Path("/api/organization-memberships")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class OrganizationMembershipResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationMembershipResource.class);

    private static final String ENTITY_NAME = "organizationMembership";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    OrganizationMembershipService organizationMembershipService;

    /**
     * {@code POST  /organization-memberships} : Create a new organizationMembership.
     *
     * @param organizationMembershipDTO the organizationMembershipDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new organizationMembershipDTO, or with status {@code 400 (Bad Request)} if the organizationMembership has already an ID.
     */
    @POST
    public Response createOrganizationMembership(@Valid OrganizationMembershipDTO organizationMembershipDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save OrganizationMembership : {}", organizationMembershipDTO);
        if (organizationMembershipDTO.id != null) {
            throw new BadRequestAlertException("A new organizationMembership cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = organizationMembershipService.persistOrUpdate(organizationMembershipDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /organization-memberships} : Updates an existing organizationMembership.
     *
     * @param organizationMembershipDTO the organizationMembershipDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated organizationMembershipDTO,
     * or with status {@code 400 (Bad Request)} if the organizationMembershipDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organizationMembershipDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateOrganizationMembership(@Valid OrganizationMembershipDTO organizationMembershipDTO, @PathParam("id") Long id) {
        log.debug("REST request to update OrganizationMembership : {}", organizationMembershipDTO);
        if (organizationMembershipDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = organizationMembershipService.persistOrUpdate(organizationMembershipDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organizationMembershipDTO.id.toString()).forEach(
            response::header
        );
        return response.build();
    }

    /**
     * {@code DELETE  /organization-memberships/:id} : delete the "id" organizationMembership.
     *
     * @param id the id of the organizationMembershipDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteOrganizationMembership(@PathParam("id") Long id) {
        log.debug("REST request to delete OrganizationMembership : {}", id);
        organizationMembershipService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /organization-memberships} : get all the organizationMemberships.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of organizationMemberships in body.
     */
    @GET
    public Response getAllOrganizationMemberships(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo
    ) {
        log.debug("REST request to get a page of OrganizationMemberships");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<OrganizationMembershipDTO> result = organizationMembershipService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /organization-memberships/:id} : get the "id" organizationMembership.
     *
     * @param id the id of the organizationMembershipDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the organizationMembershipDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getOrganizationMembership(@PathParam("id") Long id) {
        log.debug("REST request to get OrganizationMembership : {}", id);
        Optional<OrganizationMembershipDTO> organizationMembershipDTO = organizationMembershipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organizationMembershipDTO);
    }
}
