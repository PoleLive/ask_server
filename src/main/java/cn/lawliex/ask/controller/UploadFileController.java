package cn.lawliex.ask.controller;
import cn.lawliex.ask.model.User;
import cn.lawliex.ask.service.UserService;
import cn.lawliex.ask.util.JsonUtil;
import org.aspectj.apache.bcel.util.ClassLoaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Terence on 2017/1/1.
 */
@Controller
public class UploadFileController {

    public static final String ROOT = "upload_dir";
    public static final String IMG = "img_tmp_dir";
    private final ResourceLoader resourceLoader;

    @Autowired
    public UploadFileController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Autowired
    UserService userService;
//    @RequestMapping(path = {"/index", "/"}, method = {RequestMethod.GET})
//    public String index() {
//        return "index";
//    }


    @RequestMapping(method = RequestMethod.GET, value = "/{filename:.+}")
    @ResponseBody
    public ResponseEntity<?> getFile(@PathVariable String filename) {

        try {
            return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(ROOT, filename).toString()));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @RequestMapping(method = RequestMethod.GET, value = "/msgimg/{filename:.+}")
    @ResponseBody
    public ResponseEntity<?> getMsgImg(@PathVariable String filename) {

        try {
            return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(IMG, filename).toString()));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    @ResponseBody
    public String handleFileUpload(@RequestParam("file") MultipartFile file,@RequestParam("ticket")String ticket) {
        if (!file.isEmpty()) {
            try {
                File dir = new File(ROOT);
                if(dir.exists()== false){
                    dir.mkdir();
                }
                User user = userService.getUserByTicket(ticket);
                //Files.copy(file.getInputStream(), Paths.get(ROOT, file.getOriginalFilename()));
                String fileName = file.getOriginalFilename();
                String headFile = user.getId() + "." + fileName.substring(fileName.indexOf(".") + 1);

                File tmp = new File(ROOT,headFile);
                if(tmp.exists()){
                    tmp.delete();
                }
                Files.copy(file.getInputStream(), Paths.get(ROOT, headFile));
                user.setHeadUrl(headFile);
                userService.updateHeadUrl(user);
                return JsonUtil.getJSONString(0,"You successfully uploaded " + file.getOriginalFilename() + "!");

            } catch (IOException|RuntimeException e) {
                return JsonUtil.getJSONString(0,"Failued to upload " + file.getOriginalFilename() + " => " + e.getMessage());
            }
        } else {
            return JsonUtil.getJSONString(0,"Failed to upload " + file.getOriginalFilename() + " because it was empty");
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/sendimg")
    @ResponseBody
    public String handleImgMsg(@RequestParam("file") MultipartFile file,@RequestParam("ticket")String ticket) {
        if (!file.isEmpty()) {
            try {
                File dir = new File(IMG);
                if(dir.exists()== false){
                    dir.mkdir();
                }
                User user = userService.getUserByTicket(ticket);
                //Files.copy(file.getInputStream(), Paths.get(ROOT, file.getOriginalFilename()));
                String fileName = file.getOriginalFilename();
                Date date = new Date();
                String tmpFile = date.getTime()+ UUID.randomUUID().toString().substring(0,5) + "." + fileName.substring(fileName.indexOf(".") + 1);

                File tmp = new File(IMG,tmpFile);
                if(tmp.exists()){
                    tmp.delete();
                }
                Files.copy(file.getInputStream(), Paths.get(IMG, tmpFile));
                Map<String,Object> map = new HashMap<>();
                map.put("img",tmpFile);
                map.put("msg","Image is successfully uploaded");
                return JsonUtil.getJSONString(0,map);

            } catch (IOException|RuntimeException e) {
                return JsonUtil.getJSONString(0,"Failued to upload " + file.getOriginalFilename() + " => " + e.getMessage());
            }
        } else {
            return JsonUtil.getJSONString(0,"Failed to upload " + file.getOriginalFilename() + " because it was empty");
        }

    }


    @RequestMapping(path = {"uploadddd"}, method = {RequestMethod.POST})
    @ResponseBody
    public String heandleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("ticket") String ticket) {
        User user = userService.getUserByTicket(ticket);
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String path = "head/";
                String fileName = file.getOriginalFilename();
                fileName = fileName.substring(fileName.indexOf(".") + 1);
                fileName = "head_" + user.getId() + "." + fileName;
                File tempFile = new File(path, fileName);
                if (!tempFile.getParentFile().exists()) {
                    tempFile.getParentFile().mkdir();
                }
                if (!tempFile.exists()) {
                    tempFile.createNewFile();
                }

                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(tempFile));
                stream.write(bytes);
                stream.close();
                user.setHeadUrl(fileName);
                userService.updateHeadUrl(user);
                return JsonUtil.getJSONString(0, "You have successfully uploaded " + file.getName() + "into " + file.getName() + "-uploaded!");
            } catch (Exception e) {
                return JsonUtil.getJSONString(-1, "You are failed to upload " + file.getName() + " => " + e.getMessage());
            }
        }
        return JsonUtil.getJSONString(-1, "You are failed to upload " + file.getName() + " becase the file is empty.");
    }

//    @RequestMapping(path = "/user/head", method = {RequestMethod.GET})
//    public String getPhoto(@RequestParam("userId") int userId) {
//        String headUrl = userService.getUser(userId).getHeadUrl();
//        String path = "/head/" ;
//        return path;
//    }
}
