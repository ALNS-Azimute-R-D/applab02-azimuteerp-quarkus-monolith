package de.org.dexterity.azimuteerp.monolith.quarkus.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.OrganizationAttributeService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.OrganizationAttributeDTO;
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
 * REST controller for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationAttribute}.
 */
@Path("/api/organization-attributes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class OrganizationAttributeResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationAttributeResource.class);

    private static final String ENTITY_NAME = "organizationAttribute";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    OrganizationAttributeService organizationAttributeService;

    /**
     * {@code POST  /organization-attributes} : Create a new organizationAttribute.
     *
     * @param organizationAttributeDTO the organizationAttributeDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new organizationAttributeDTO, or with status {@code 400 (Bad Request)} if the organizationAttribute has already an ID.
     */
    @POST
    public Response createOrganizationAttribute(@Valid OrganizationAttributeDTO organizationAttributeDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save OrganizationAttribute : {}", organizationAttributeDTO);
        if (organizationAttributeDTO.id != null) {
            throw new BadRequestAlertException("A new organizationAttribute cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = organizationAttributeService.persistOrUpdate(organizationAttributeDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /organization-attributes} : Updates an existing organizationAttribute.
     *
     * @param organizationAttributeDTO the organizationAttributeDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated organizationAttributeDTO,
     * or with status {@code 400 (Bad Request)} if the organizationAttributeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organizationAttributeDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateOrganizationAttribute(@Valid OrganizationAttributeDTO organizationAttributeDTO, @PathParam("id") Long id) {
        log.debug("REST request to update OrganizationAttribute : {}", organizationAttributeDTO);
        if (organizationAttributeDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = organizationAttributeService.persistOrUpdate(organizationAttributeDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organizationAttributeDTO.id.toString()).forEach(
            response::header
        );
        return response.build();
    }

    /**
     * {@code DELETE  /organization-attributes/:id} : delete the "id" organizationAttribute.
     *
     * @param id the id of the organizationAttributeDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteOrganizationAttribute(@PathParam("id") Long id) {
        log.debug("REST request to delete OrganizationAttribute : {}", id);
        organizationAttributeService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /organization-attributes} : get all the organizationAttributes.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of organizationAttributes in body.
     */
    @GET
    public Response getAllOrganizationAttributes(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo
    ) {
        log.debug("REST request to get a page of OrganizationAttributes");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<OrganizationAttributeDTO> result = organizationAttributeService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /organization-attributes/:id} : get the "id" organizationAttribute.
     *
     * @param id the id of the organizationAttributeDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the organizationAttributeDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getOrganizationAttribute(@PathParam("id") Long id) {
        log.debug("REST request to get OrganizationAttribute : {}", id);
        Optional<OrganizationAttributeDTO> organizationAttributeDTO = organizationAttributeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organizationAttributeDTO);
    }
}
