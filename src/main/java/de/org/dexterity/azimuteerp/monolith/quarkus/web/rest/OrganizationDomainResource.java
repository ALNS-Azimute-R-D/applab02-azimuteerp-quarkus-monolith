package de.org.dexterity.azimuteerp.monolith.quarkus.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.OrganizationDomainService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.OrganizationDomainDTO;
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
 * REST controller for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationDomain}.
 */
@Path("/api/organization-domains")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class OrganizationDomainResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationDomainResource.class);

    private static final String ENTITY_NAME = "organizationDomain";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    OrganizationDomainService organizationDomainService;

    /**
     * {@code POST  /organization-domains} : Create a new organizationDomain.
     *
     * @param organizationDomainDTO the organizationDomainDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new organizationDomainDTO, or with status {@code 400 (Bad Request)} if the organizationDomain has already an ID.
     */
    @POST
    public Response createOrganizationDomain(@Valid OrganizationDomainDTO organizationDomainDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save OrganizationDomain : {}", organizationDomainDTO);
        if (organizationDomainDTO.id != null) {
            throw new BadRequestAlertException("A new organizationDomain cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = organizationDomainService.persistOrUpdate(organizationDomainDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /organization-domains} : Updates an existing organizationDomain.
     *
     * @param organizationDomainDTO the organizationDomainDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated organizationDomainDTO,
     * or with status {@code 400 (Bad Request)} if the organizationDomainDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organizationDomainDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateOrganizationDomain(@Valid OrganizationDomainDTO organizationDomainDTO, @PathParam("id") Long id) {
        log.debug("REST request to update OrganizationDomain : {}", organizationDomainDTO);
        if (organizationDomainDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = organizationDomainService.persistOrUpdate(organizationDomainDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organizationDomainDTO.id.toString()).forEach(
            response::header
        );
        return response.build();
    }

    /**
     * {@code DELETE  /organization-domains/:id} : delete the "id" organizationDomain.
     *
     * @param id the id of the organizationDomainDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteOrganizationDomain(@PathParam("id") Long id) {
        log.debug("REST request to delete OrganizationDomain : {}", id);
        organizationDomainService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /organization-domains} : get all the organizationDomains.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of organizationDomains in body.
     */
    @GET
    public Response getAllOrganizationDomains(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo
    ) {
        log.debug("REST request to get a page of OrganizationDomains");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<OrganizationDomainDTO> result = organizationDomainService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /organization-domains/:id} : get the "id" organizationDomain.
     *
     * @param id the id of the organizationDomainDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the organizationDomainDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getOrganizationDomain(@PathParam("id") Long id) {
        log.debug("REST request to get OrganizationDomain : {}", id);
        Optional<OrganizationDomainDTO> organizationDomainDTO = organizationDomainService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organizationDomainDTO);
    }
}
