/*******************************************************************************
 * 2008-2011 Projecto Colibri
 * Marco Lopes (marcolopes@projectocolibri.com)
 *******************************************************************************/
package org.projectocolibri.rcp.magento.dao.manager;

import java.util.Collection;

import com.google.code.magja.model.product.Product;
import com.google.code.magja.model.product.ProductType;
import com.google.code.magja.service.RemoteServiceFactory;
import com.google.code.magja.service.product.ProductRemoteService;

import org.dma.java.math.NumericUtils;
import org.dma.java.util.StringUtils;

public class ProductManager {

	private final ProductRemoteService remoteService=RemoteServiceFactory.getSingleton().getProductRemoteService();

	public boolean existsProduct(String sku) {

		System.out.println(sku);

		try{
			remoteService.getBySku(sku);
			return true;

		}catch(Exception e){}

		return false;

	}


	public void saveProduct(Product product, Product existingProduct) {

		System.out.println(product);

		try{
			if (existingProduct==null){
				remoteService.add(product);
				System.out.println("CREATED");
			}else{
				remoteService.update(product,existingProduct);
				System.out.println("UPDATED");
			}

		}catch(Exception e){
			e.printStackTrace();
			System.out.println("EXISTS");
		}
	}


	public void createProduct(String sku) {

		System.out.println(sku);

		try{
			Product product=new Product();

			product.setSku(sku);
			product.setName(StringUtils.capitalize(StringUtils.randomLetters(10)));
			product.setShortDescription(StringUtils.capitalize(StringUtils.randomLetters(25)));
			product.setDescription(StringUtils.capitalize(StringUtils.randomLetters(100)));
			product.setPrice(new Double(NumericUtils.random(3)));
			product.setCost(new Double(NumericUtils.random(3)));
			product.setEnabled(true);
			product.setWeight(new Double(NumericUtils.random(2)));
			product.setType(ProductType.SIMPLE);
			//product.setAttributeSet(new ProductAttributeSet(4, "Default"));
			product.setMetaDescription(StringUtils.randomLetters(20));
			product.setGoogleCheckout(true);

			/*
			// categories
			List<Category> categories=new ArrayList();
			categories.add(new Category("Hardware"));
			categories.add(new Category("Computers"));
			product.setCategories(categories);
			*/

			// websites
			Integer[] websites={1};
			product.setWebsites(websites);

			// inventory
			product.setQty(new Double(NumericUtils.random(2)));
			product.setInStock(true);

			/*
			// images
			Media image=new Media();
			image.setName("google");
			image.setMime("image/jpeg");
			image.setData(MagjaFileUtils.getBytesFromFileURL("http://code.google.com/images/code_sm.png"));

			Set<ProductMedia.Type> types=new HashSet<ProductMedia.Type>();
			types.add(ProductMedia.Type.IMAGE);
			types.add(ProductMedia.Type.SMALL_IMAGE);

			ProductMedia media=new ProductMedia();
			media.setExclude(false);
			media.setImage(image);
			media.setLabel("Image for Product");
			media.setPosition(1);
			media.setTypes(types);

			product.addMedia(media);
			*/

			/*
			// linked products
			ProductLink link=new ProductLink();
			link.setId("ID_OR_SKU_OF_LINKED");
			link.setPosition(1);
			link.setQty(new Double(10));
			link.setLinkType(LinkType.RELATED);

			product.addLink(link);
			*/

			/*
			// Optional: you can set the properties not mapped like the following too:
			product.set("meta_description", "one two tree");
			product.set("enable_googlecheckout", 1);
			*/

			saveProduct(product,null);

		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void listProducts() {

		try{
			Collection<Product> products=remoteService.listAllNoDep();
			for (Product product : products) {
				System.out.println(product);
			}

			System.out.println(products.size());

		}catch(Exception e){
			e.printStackTrace();
		}

	}


}