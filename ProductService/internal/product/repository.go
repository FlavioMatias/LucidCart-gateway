package product

import "strings"

type MockRepository struct {
	data []Product
}

func NewMockRepository() *MockRepository {
	return &MockRepository{
		data: []Product{
			{
				ID: 1, Name: "Livro: Dominando Go",
				Description: "Guia completo e moderno da linguagem Go.",
				Price: 89.90,
				Image: "https://www.casadocodigo.com.br/cdn/shop/products/ProgramandoemGo_ebook_large.jpg?v=1631651513",
				Stock: 12, Category: "Livro",
			},
			{
				ID: 2, Name: "Livro: Kotlin Moderno",
				Description: "Kotlin aplicado a backend e Android.",
				Price: 94.90,
				Image: "https://www.casadocodigo.com.br/cdn/shop/products/KotlincomAndroid_ebook_large.jpg?v=1631656393",
				Stock: 10, Category: "Livro",
			},
			{
				ID: 3, Name: "Livro: Node.js Essencial",
				Description: "Estruturação profissional de serviços com Node.",
				Price: 79.90,
				Image: "https://images-na.ssl-images-amazon.com/images/I/71y8nbSxa4L._AC_UL900_SR615,900_.jpg",
				Stock: 15, Category: "Livro",
			},
			{
				ID: 4, Name: "Livro: Arquitetura Distribuída",
				Description: "Microserviços, mensageria, escalabilidade e resiliência.",
				Price: 149.90,
				Image: "https://www.casadocodigo.com.br/cdn/shop/products/p_large.jpg?v=1634930297",
				Stock: 7, Category: "Livro",
			},

			{
				ID: 5, Name: "Café Gourmet Torrado",
				Description: "Combustível oficial do estudante.",
				Price: 32.00,
				Image: "https://m.media-amazon.com/images/I/71vxs0NBiUL._AC_UF894,1000_QL80_.jpg",
				Stock: 25, Category: "Comidas",
			},
			{
				ID: 6, Name: "Copo Térmico Aço 500ml",
				Description: "Mantém café quente por horas, perfeito pra madrugada.",
				Price: 79.90,
				Image: "https://cdn.iset.io/assets/51664/produtos/2209/ec1ffb9033e76b4e5f6f58a83e52b12a65aae0894bf96.png",
				Stock: 18, Category: "Acessórios",
			},
			{
				ID: 7, Name: "Caderno Pontilhado Premium",
				Description: "Perfeito para diagramas, anotações e estudos.",
				Price: 24.90,
				Image: "https://http2.mlstatic.com/D_NQ_NP_716738-MLB80311320727_102024-O-caderno-sketchbook-pontilhado-classico-executivo-premium.webp",
				Stock: 40, Category: "Papelaria",
			},
			{
				ID: 8, Name: "Caneta Gel 0.5 Preta",
				Description: "Escrita macia pra quem rabisca UML o tempo todo.",
				Price: 6.90,
				Image: "https://cdn.awsli.com.br/600x700/765/765263/produto/140738243/ca2451afbf.jpg",
				Stock: 100, Category: "Papelaria",
			},
			{
				ID: 9, Name: "Headset Conforto Pro",
				Description: "Ideal pra estudar com Lo-Fi ou participar de meet.",
				Price: 129.90,
				Image: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTPRkCBUuiXiwvvoxeo_fMX7GHu3UsR4vMomQ&s",
				Stock: 14, Category: "Acessórios",
			},
			{
				ID: 10, Name: "Suporte Ergonômico para Notebook",
				Description: "Evita dor nas costas depois de 6h de código.",
				Price: 54.90,
				Image: "https://m.media-amazon.com/images/I/51GMrrZ-Z0L._AC_UF1000,1000_QL80_.jpg",
				Stock: 20, Category: "Ergonomia",
			},
			{
				ID: 11, Name: "Luminária de Mesa LED",
				Description: "Luz neutra ajustável para estudo noturno.",
				Price: 69.90,
				Image: "https://lojatoquedecasa.com.br/cdn/shop/files/luminaria-de-mesa-led-protecao-dos-olhos-toque-de-casa-3_750x.jpg?v=1721496880",
				Stock: 22, Category: "Iluminação",
			},
			{
				ID: 12, Name: "Mochila Minimalista Universitária",
				Description: "Leve, resistente, cabe notebook e livros.",
				Price: 119.90,
				Image: "https://lojaviego.com.br/cdn/shop/files/MochilaMinimalistaparaNotebookModeloSlim_4.webp?v=1707767694",
				Stock: 13, Category: "Acessórios",
			},
			{
				ID: 13, Name: "Garrafa de Água 1L",
				Description: "Hidratação enquanto codifica = cérebro funcionando.",
				Price: 29.90,
				Image: "https://mercantilnovaera.vtexassets.com/arquivos/ids/199097/Agua-Sanitaria-BRILUX-Garrafa-1L.jpg?v=637916175931300000",
				Stock: 30, Category: "Acessórios",
			},
			{
				ID: 14, Name: "Agenda Semanal Minimalista",
				Description: "Organização pra quem tem 8 projetos ao mesmo tempo.",
				Price: 19.90,
				Image: "https://img.elo7.com.br/product/zoom/516FFE5/planner-semanal-minimalista-para-imprimir-agenda.jpg",
				Stock: 50, Category: "Papelaria",
			},
			{
				ID: 15, Name: "Barra de Cereal Premium",
				Description: "Lanche rápido entre uma aula e outra.",
				Price: 3.90,
				Image: "https://destro.fbitsstatic.net/img/p/barra-de-cereal-nutry-sabor-cookiescream-20g-85170/271734-1.jpg?w=500&h=500&v=202501231555&qs=ignore",
				Stock: 70, Category: "Comidas",
			},
			{
				ID: 16, Name: "Chinelo Slide Confort",
				Description: "Pé descansado = mente descansada.",
				Price: 39.90,
				Image: "https://images.tcdn.com.br/img/img_prod/1356993/chinelo_slide_rider_smash_v_conforto_anatmico_masc_1_20251017114421_5cbac682fb4a.jpg",
				Stock: 18, Category: "Vestuário",
			},
			{
				ID: 17, Name: "Pulseira Inteligente FitBand",
				Description: "Monitore sono e batimentos. Programar morto não dá.",
				Price: 129.00,
				Image: "https://down-br.img.susercontent.com/file/sg-11134201-7rbn1-lomci1qka4qyb7",
				Stock: 9, Category: "Smart",
			},
			{
				ID: 18, Name: "Almofada Ortopédica",
				Description: "Pra quem passa horas sentado compilando erros.",
				Price: 49.90,
				Image: "https://m.media-amazon.com/images/I/71nfS8mZfML._AC_UF894,1000_QL80_.jpg",
				Stock: 16, Category: "Ergonomia",
			},
			{
				ID: 19, Name: "Quadro Decorativo Minimalista",
				Description: "Ambiente bonito ajuda a pensar melhor.",
				Price: 34.90,
				Image: "https://www.poesiamuda.com.br/media/catalog/product/cache/1/image/400x/9df78eab33525d08d6e5fb8d27136e95/q/u/quadro-decorativo-minimalista-bando-de-passaros-pousados-em-fios-preto-e-branco-para-sala.jpg",
				Stock: 12, Category: "Decoração",
			},
			{
				ID: 20, Name: "Fone Bluetooth Compacto",
				Description: "Pra escutar podcasts de tecnologia no ônibus.",
				Price: 89.90,
				Image: "https://a-static.mlcdn.com.br/800x600/fone-bluetooth-sem-fio-ouvido-foninho-microfone-original-01smart/01esporte/cp8-fone-a6s-pr/cba8435d047ef1bfe8253d5968824b50.jpeg",
				Stock: 21, Category: "Acessórios",
			},
		},
	}
}

func (r *MockRepository) List() []Product { return r.data }

func (r *MockRepository) FindByID(id int) *Product {
	for i := range r.data {
		if r.data[i].ID == id {
			return &r.data[i]
		}
	}
	return nil
}

func (r *MockRepository) Search(q string) []Product {
	if q == "" {
		return r.data
	}

	q = strings.ToLower(q)
	res := []Product{}

	for _, p := range r.data {
		name := strings.ToLower(p.Name)
		cat := strings.ToLower(p.Category)

		if strings.Contains(name, q) {
			res = append(res, p)
			continue
		}

		if strings.Contains(cat, q) {
			res = append(res, p)
		}
	}

	return res
}

