@RestController
class HomeController {

	@RequestMapping("/")
	String home() {
		'Hello world!'
	}
}