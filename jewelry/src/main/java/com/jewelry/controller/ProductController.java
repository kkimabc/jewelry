package com.jewelry.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.jewelry.common.Util;
import com.jewelry.service.ProductService;
import com.jewelry.vo.DetailVo;
import com.jewelry.vo.OrderVo;
import com.jewelry.vo.ProductImgVo;
import com.jewelry.vo.ProductVo;
import com.jewelry.vo.SalesVo;
import com.jewelry.vo.orderViewVo;

@Controller
@RequestMapping("/product/")
public class ProductController {
	
	@Autowired
	@Qualifier("productService")
	private ProductService productService;
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
	
	/************************************************************************/
	
	//제품
	@RequestMapping(value="product.action",method=RequestMethod.GET)
	public String productView(int storeNo,Model model) {
		
		List<ProductVo> products = productService.takeAllProduct(storeNo);
		
		model.addAttribute("products",products);
			
		return "product/product";
	}
	
	//제품등록
	@RequestMapping(value="productupload.action",method=RequestMethod.GET)
	public String productUploadView() {
		
		return "product/productupload";
	}
	
	@RequestMapping(value="productupload.action",method=RequestMethod.POST)
	public String productupload(ProductVo productVo,MultipartHttpServletRequest req,Model model) {
		
			MultipartFile img = req.getFile("img");
			ArrayList<ProductImgVo> imgs = new ArrayList<>();
			
			if(img !=null && !img.isEmpty()) {
				String savedname = Util.makeUniqueFileName(img.getOriginalFilename());
				String path = req.getServletContext().getRealPath("/resources/product-img/" + savedname);

				
				try {
					img.transferTo(new File(path));
					ProductImgVo productImg = new ProductImgVo();
					productImg.setImgSavedName(savedname);
					productImg.setImgFileName(img.getOriginalFilename());		
					imgs.add(productImg);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			productVo.setImgs(imgs);
			productService.insertProduct(productVo);		
			model.addAttribute("storeNo", productVo.getUserNo());
				
			return "redirect:/product/product.action";
		}
	
	//제품수정
	@RequestMapping(value="productUd.action",method=RequestMethod.POST)
	public String productUd(ProductVo productVo,MultipartHttpServletRequest req) {
		
			MultipartFile img = req.getFile("img");
			ArrayList<ProductImgVo> imgs = new ArrayList<>();
			
			if(img !=null && !img.isEmpty()) {
				String savedname = Util.makeUniqueFileName(img.getOriginalFilename());
				String path = req.getServletContext().getRealPath("/resources/product-img/" + savedname);

				
				try {
					img.transferTo(new File(path));
					ProductImgVo productImg = new ProductImgVo();
					productImg.setImgSavedName(savedname);
					productImg.setImgFileName(img.getOriginalFilename());		
					imgs.add(productImg);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			productVo.setImgs(imgs);
			productService.updateProduct(productVo);	
				
			return "redirect:/product/product.action";
		}
	
	//제품삭제
	@RequestMapping(value="productDel.action",method=RequestMethod.POST)
	public String prodcutDel(int productNo) {
		
		productService.deleteProduct(productNo);
		
		return "redirect:/product/product.action";
	}
	
	/************************************************************************/
	
	//주문
	@RequestMapping(value="order.action",method=RequestMethod.GET)
	public String OrderView(int storeNo,Model model) {
		
		List<orderViewVo> views=productService.selectAllOrderList(storeNo);
		model.addAttribute("views",views);
		
		return "product/order";
	}
	
	//제품주문수정
	@RequestMapping(value="orderUpdate.action",method=RequestMethod.POST)
	public String orderUpdate(DetailVo detailVo,OrderVo orderVo) {
			
		ArrayList<OrderVo> orders = new ArrayList<>();
		orders.add(orderVo);
		detailVo.setOrders(orders);
			
		productService.updateOrder(detailVo);
			
		return "redirect:/product/product.action";
	}
		
	//제품주문취소
	@RequestMapping(value="orderDel.action",method=RequestMethod.POST)
	public String orderDel(int orderNo) {
			
		productService.orderDel(orderNo);
			
		return "redirect:/product/product.action";
	}
	
	//주문상태변경
	@RequestMapping(value="ordertype.action",method=RequestMethod.POST)
	@ResponseBody
	public String ordertype(int orderNo,String type,String div,SalesVo sales) {
		
		if(div.equals("주문판매")) {
			productService.insertSales(sales);
		}else {
			productService.updateStockMount(sales.getDetailNo(),sales.getSalesCount());
		}
		productService.changeOrderViewType(orderNo,type);
			
		return "success";
	}
	
	//주문리스트
	@RequestMapping(value="orderVIewList.action",method=RequestMethod.POST)
	public String orderViewList(int storeNo,Model model) {
			
		List<orderViewVo> views=productService.selectAllOrderList(storeNo);
		model.addAttribute("views",views);
			
		return "product/orderviewlist";
	}
		
	/************************************************************************/
	
	//제품상세
	@RequestMapping(value="detail.action",method=RequestMethod.GET)
	public String detailView(int productNo,Model model) {
		
		ProductVo product=productService.takeProductByProductNo(productNo);
		model.addAttribute("product",product);
		
		return "product/detail";
	}
	
	//주문등록
	@RequestMapping(value="detail.action",method=RequestMethod.POST)
	public String productOrder(DetailVo detailVo,OrderVo orderVo,int storeNo,Model model) {

		ArrayList<OrderVo> orders = new ArrayList<>();
		orders.add(orderVo);
		detailVo.setOrders(orders);
		
		productService.insertOrder(detailVo);
		model.addAttribute("storeNo",storeNo);
		
		return "redirect:/product/product.action";
	}		
	
	/************************************************************************/
	
	//재고
	@RequestMapping(value="stock.action",method=RequestMethod.GET)
	public String stockView(int storeNo,Model model) {
		
		List<ProductVo> products = productService.takeAllStockList(storeNo);
		model.addAttribute("products", products);
		
		return "/product/stock";
	}
	
	//재고추가
	@RequestMapping(value="stockupload.action",method=RequestMethod.GET)
	public String stockUploadView(int storeNo,Model model) {
		
		List<ProductVo> products = productService.takeAllProduct(storeNo);
		
		model.addAttribute("products",products);
		
		return "/product/stockupload";
	}
	
	@RequestMapping(value="stockupload.action",method=RequestMethod.POST)
	public String stockUpload(DetailVo detailVo,int storeNo,Model model) {
		
		productService.insertStock(detailVo);
		model.addAttribute("storeNo", storeNo);
		
		return "redirect:/product/stock.action";
	}
	
	
	/************************************************************************/
	
	//판매
	@RequestMapping(value="sales.action",method=RequestMethod.GET)
	public String salesView() {
		
		//조회
		return "/product/sales";
	}
	
}
