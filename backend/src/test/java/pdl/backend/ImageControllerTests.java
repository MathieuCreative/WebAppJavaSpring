package pdl.backend;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class ImageControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@BeforeAll
	public static void reset() {
  	// reset Image class static counter
  	ReflectionTestUtils.setField(Image.class, "count", Long.valueOf(0));
	}

	@Test
	@Order(1)
	public void getImageListShouldReturnSuccess() throws Exception {
		this.mockMvc.perform(get("/images"))
		.andExpect(status().isOk());
		}

	@Test
	@Order(2)
	public void getImageShouldReturnNotFound() throws Exception {
		this.mockMvc.perform(get("/images/15"))
		.andDo(print())
		.andExpect(status().isNotFound());
	}

	@Test
	@Order(3)
	public void getImageShouldReturnSuccess() throws Exception {
		long imageId =0; 

		this.mockMvc.perform(get("/images/" + imageId))
		.andDo(print())
		.andExpect(status().isOk());	
	}

		
	@Test
	@Order(4)
	public void deleteImagesShouldReturnMethodNotAllowed() throws Exception {
		this.mockMvc.perform(delete("/images/"))
		.andDo(print())
		.andExpect(status().isMethodNotAllowed());		
	}

	@Test
	@Order(5)
	public void deleteImageShouldReturnNotFound() throws Exception {
		long imageId = 10; 
		this.mockMvc.perform(get("/images/" + imageId))
		.andDo(print())
		.andExpect(status().isNotFound());	
	}

	@Test
	@Order(6)
	public void deleteImageShouldReturnSuccess() throws Exception {
		long imageId = 0;

		this.mockMvc.perform(delete("/images/" + imageId))
		.andDo(print())
		.andExpect(status().isNoContent());

		this.mockMvc.perform(get("/images/" + imageId))
		.andDo(print())
		.andExpect(status().isNotFound());	
	}

	@Test
	@Order(7)
	public void createImageShouldReturnSuccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "image/jpeg", "some xml".getBytes());
        mockMvc.perform(multipart("/images").file(file))
               .andExpect(status().isOk());
	}

	@Test
	@Order(8)
	public void createImageShouldReturnUnsupportedMediaType() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "application/json", "{\"key1\": \"value1\"}".getBytes());
        mockMvc.perform(multipart("/images").file(file))
               .andExpect(status().isUnsupportedMediaType());	
			}
	
}
