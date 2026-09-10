package lk.ijse.preordersystem.controllers;

import lk.ijse.preordersystem.dto.CommonResponse;
import lk.ijse.preordersystem.dto.RoleDTO;
import lk.ijse.preordersystem.entity.Role;
import lk.ijse.preordersystem.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "v1/role")
@RequiredArgsConstructor
@Slf4j
public class RoleController {

    private final RoleRepository roleRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllRoles() {

        log.info("getAllRoles API was called");

        List<RoleDTO> responseList = new ArrayList<>();
        List<Role> roleList = roleRepository.findAll();

        for (Role role : roleList) {

            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setRoleId(role.getRoleId());
            roleDTO.setRoleName(role.getRoleName());
            roleDTO.setDescription(role.getDescription());

            responseList.add(roleDTO);
        }

        log.info("getAllRoles API successful");
        return new CommonResponse(0, responseList, "Roles called");
    }
}
