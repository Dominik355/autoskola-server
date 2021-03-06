
package com.example.AutoskolaDemoWithSecurity.controllers;

import com.example.AutoskolaDemoWithSecurity.models.transferModels.UpdateEmailRequest;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.UpdatePasswordRequest;
import com.example.AutoskolaDemoWithSecurity.services.DrivingSchoolService;
import com.example.AutoskolaDemoWithSecurity.services.MyUserDetailsService;
import com.example.AutoskolaDemoWithSecurity.services.PictureService;
import com.example.AutoskolaDemoWithSecurity.services.RelationshipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@Api(value = "Dostupne pre vsetkych prihlasenych uzivatelov")
@RequestMapping(value = {"/user"})
public class UserController
{
    
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    
    @Autowired
    private PictureService pictureService;
    
    @Autowired
    private RelationshipService relationService;
    
    @Autowired
    private DrivingSchoolService schoolService;
    
    
    @PostMapping({"/update/password"})
    @ApiOperation(value = "${userController.updatePassword.value}",
            response = ResponseEntity.class)
    public ResponseEntity updatePassword(@RequestBody @Valid UpdatePasswordRequest updatePasswordRequest) {
        return ResponseEntity.ok(this.myUserDetailsService.updatePassword(updatePasswordRequest)); 
    }

    
    @PostMapping({"/update/email"})
    @ApiOperation(value = "${userController.updateEmail.value}",
            response = ResponseEntity.class)
    public ResponseEntity updateEmail(@RequestBody @Valid UpdateEmailRequest updateEmailRequest) {
        return ResponseEntity.ok(this.myUserDetailsService.updateEmail(updateEmailRequest)); 
    }
    
    
    @GetMapping({"/viewRelations"})
    @ApiOperation(value = "${userController.viewRelations.value}",
                  notes = "${userController.viewRelations.notes}")
    public ResponseEntity viewRelations() {
        return ResponseEntity.ok(relationService.getAllRelations(""));
    }

    @PostMapping("/logOut")
    @ApiOperation(value = "${userController.logOut.value}")
    public ResponseEntity logOut() {
        return new ResponseEntity(myUserDetailsService.logOut(), HttpStatus.OK);
    }
    
    @GetMapping({"/getSchools"})
    @ApiOperation(value = "${userController.getSchools.value}")
    public ResponseEntity getSchools() {
        return ResponseEntity.ok(schoolService.getDrivingSchools());
    }
    
    @PostMapping("/savePicture")
    @ApiOperation(value = "${userController.saveProfilePicture.value}")
    public ResponseEntity saveProfilePicture(@RequestBody MultipartFile imageFile) throws IOException {
        return ResponseEntity.ok(pictureService.saveImage(imageFile));
    }
    
    @GetMapping(value = {"/getPicture"}, produces = MediaType.IMAGE_PNG_VALUE)
    @ApiOperation(value = "${userController.getPicture.value}",
            notes = "${userController.getPicture.notes}",
            response = byte[].class)
    public ResponseEntity<byte[]> getPicture() throws FileNotFoundException{
        return ResponseEntity.ok(pictureService.getFile().getPicture()); 
    }
    
}