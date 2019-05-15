package com.forezp.web;

import com.forezp.entity.Blog;
import com.forezp.service.impl.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by fangzhipeng on 2017/5/15.
 */

@RestController
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    BlogService blogService;

    @GetMapping
    public ModelAndView list(Model model) {
        List<Blog> list = blogService.getBlogs();
        model.addAttribute("blogsList", list);
        return new ModelAndView("blogs/list", "blogModel", model);
    }

    /**
     * 需要“ADMIN”的角色权限，通过注解＠PreAuthorize来实现。在调用删除Blog接口之前，会判断该用户是否具有权限。
     * 如果有权限，则可以删除，如果没有权限，则显示权限不足的界面
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")  //
    @GetMapping(value = "/{id}/deletion")
    public ModelAndView delete(@PathVariable("id") Long id, Model model) {
        blogService.deleteBlog(id);
        model.addAttribute("blogsList", blogService.getBlogs());
        return new ModelAndView("blogs/list", "blogModel", model);
    }
}
