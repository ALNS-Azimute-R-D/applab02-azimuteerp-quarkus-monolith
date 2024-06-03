package de.org.dexterity.azimuteerp.monolith.quarkus.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.TypeOfPersonService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.TypeOfPersonDTO;
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
 * REST controller for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.TypeOfPerson}.
 */
@Path("/api/type-of-people")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class TypeOfPersonResource {

    private final Logger log = LoggerFactory.getLogger(TypeOfPersonResource.class);

    private static final String ENTITY_NAME = "typeOfPerson";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    TypeOfPersonService typeOfPersonService;

    /**
     * {@code POST  /type-of-people} : Create a new typeOfPerson.
     *
     * @param typeOfPersonDTO the typeOfPersonDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new typeOfPersonDTO, or with status {@code 400 (Bad Request)} if the typeOfPerson has already an ID.
     */
    @POST
    public Response createTypeOfPerson(@Valid TypeOfPersonDTO typeOfPersonDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save TypeOfPerson : {}", typeOfPersonDTO);
        if (typeOfPersonDTO.id != null) {
            throw new BadRequestAlertException("A new typeOfPerson cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = typeOfPersonService.persistOrUpdate(typeOfPersonDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /type-of-people} : Updates an existing typeOfPerson.
     *
     * @param typeOfPersonDTO the typeOfPersonDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated typeOfPersonDTO,
     * or with status {@code 400 (Bad Request)} if the typeOfPersonDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeOfPersonDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateTypeOfPerson(@Valid TypeOfPersonDTO typeOfPersonDTO, @PathParam("id") Long id) {
        log.debug("REST request to update TypeOfPerson : {}", typeOfPersonDTO);
        if (typeOfPersonDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = typeOfPersonService.persistOrUpdate(typeOfPersonDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeOfPersonDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /type-of-people/:id} : delete the "id" typeOfPerson.
     *
     * @param id the id of the typeOfPersonDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteTypeOfPerson(@PathParam("id") Long id) {
        log.debug("REST request to delete TypeOfPerson : {}", id);
        typeOfPersonService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /type-of-people} : get all the typeOfPeople.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of typeOfPeople in body.
     */
    @GET
    public Response getAllTypeOfPeople(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo
    ) {
        log.debug("REST request to get a page of TypeOfPeople");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<TypeOfPersonDTO> result = typeOfPersonService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /type-of-people/:id} : get the "id" typeOfPerson.
     *
     * @param id the id of the typeOfPersonDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the typeOfPersonDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getTypeOfPerson(@PathParam("id") Long id) {
        log.debug("REST request to get TypeOfPerson : {}", id);
        Optional<TypeOfPersonDTO> typeOfPersonDTO = typeOfPersonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeOfPersonDTO);
    }
}
