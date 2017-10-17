package com.toptal.essienntaemmanuel2ndattempt.rest;

import com.toptal.essienntaemmanuel2ndattempt.domain.Calory;
import com.toptal.essienntaemmanuel2ndattempt.domain.Role;
import com.toptal.essienntaemmanuel2ndattempt.dto.CaloryDto;
import com.toptal.essienntaemmanuel2ndattempt.exception.NoSuchAccountException;
import com.toptal.essienntaemmanuel2ndattempt.service.CaloryService;
import com.toptal.essienntaemmanuel2ndattempt.util.AuthorityUtil;
import com.toptal.essienntaemmanuel2ndattempt.util.WebUtil;
import java.net.URI;
import java.security.Principal;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * @author bodmas
 */
@RestController
@RequestMapping(
        value = "/calories",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class CaloryResource {

    private static final Logger log = LoggerFactory.getLogger(CaloryResource.class);

    @Autowired
    private MapperFacade mapperFacade;

    private final CaloryService caloryService;

    public CaloryResource(CaloryService caloryService) {
        this.caloryService = caloryService;
    }

    /**
     * Add new calory. Only principals with user permissions can perform this operation.
     * @param caloryDto
     * @param fields
     * @param principal
     * @return
     */
    @PostMapping("/email/{email:.+}")
    @PreAuthorize(AuthorityUtil.HAS_USER_AUTHORITY)
    public ResponseEntity<?> create(@Valid @RequestBody CaloryDto caloryDto, BindingResult fields, Principal principal)
            throws NoSuchAccountException {
        log.info("principal name = {}", principal.getName());

        WebUtil.validate(fields);
        Calory calory = caloryService.save(principal.getName(), mapperFacade.map(caloryDto, Calory.class));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{caloryId}").buildAndExpand(calory.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{caloryId}")
    public ResponseEntity<?> findOne(@PathVariable Long caloryId, HttpServletRequest req) {
        Optional<Calory> optCalory = caloryService.findById(caloryId);
        if (optCalory.isPresent()
                && (!WebUtil.getAuthorities(req).contains(Role.USER)
                || optCalory.get().getAccount().getEmail().equals(req.getUserPrincipal().getName()))) {
            return ResponseEntity.ok(mapperFacade.map(optCalory.get(), CaloryDto.class));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
