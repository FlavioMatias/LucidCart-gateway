package product

type Service struct {
	repo *MockRepository
}

func NewService(repo *MockRepository) *Service {
	return &Service{repo: repo}
}

func (s *Service) List() []Product {
	return s.repo.List()
}

func (s *Service) FindByID(id int) *Product {
	return s.repo.FindByID(id)
}

func (s *Service) Search(query string) []Product {
	return s.repo.Search(query)
}
