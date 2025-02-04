Relatório das decisões tomadas ao longo do projeto:

-> Na descrição das funcionalidades a implementar está referido: "Reserva de quartos por parte dos hóspedes: apenas os funcionários ou gestor podem fazer reservas" -A interpretação fizemos foi: clientes podem fazer um pedido para reserva, mas fica pendente de confirmação. -Os gestores e funcionários ficam posteriormente responsáveis por confirmar esse pedido

-> No diagrama de classes está uma classe "Pagamento" , como não se exigiu autenticação achamos que não faria sentido implementar esta classe. -Pois seria necessário criar uma carteira para cada cliente (autenticação);

-> Como mostrado no diagrama de use cases, apenas o gestor do sistema tem a possibilidade de criar, remover e alterar características dos quartos. -> Gestores e funcionários têm acesso às mesmas operações -Por isso, nos testes unitários, cada método é apenas testado uma vez (ex: funcionário e gestor têm os dois a possibilidade de adicionar manutenção, esse método só é testado uma vez, já que tem a mesma implementação).

-> Foi utilizado json para persistência de dados, pois é um formato de ficheiro fácil de trabalhar e com várias bibliotecas que permitem a sua manipulação -Neste caso foi utilizado jackson para trabalhar com json. -Outra solução possível seria utilizar uma base de dados local, mas json é mais rápido e evita a criação manual de queries

-> Para os testes, foram criados 2 ficheiros extra, "reservasPendentesTeste.json" e "quartosTeste,json" -Achamos que faria sentido separar ficheiros de teste, para evitar possíveis inconsistências nos dados. -Os ficheiros contêm as reservas pendentes e os quartos, respectivamente. -Originais : "reservasPendentes.json" e "quartos.json".

-> Os únicos critérios que são utilizados para verificar se é possível efetuar uma reserva são: -Se não houver previamente uma outra reserva para o quarto na data pretendida pelo cliente; -O quarto não estar em manutenção.

-> No início do projeto pensámos adicionar uma data de início e data fim para as manutenções, no entanto, num cenário real, é muito pouco provável prever quando a manutenção irá acabar.

-> Alguns métodos estão implementados (por exemplo, verificar disponibilidade na interface do cliente), no entanto, após reflexão, percebi que talvez não fizesse sentido , pois numa situação real, um cliente apenas quer fazer reserva e não consultar a disponibilidade de um quarto (isso talvez seja apenas útil ao funcionário e gestor do hotel), além disso, nesse método é necessário fornecer o id do quarto a verificar, esses detalhes técnicos não devem ser exigidos ao cliente.

-> Método registar ocupação definido no diagrama de classes está implícito nos métodos fazer reserva / confirmar reserva, quando um cliente deseja fazer uma reserva, esse pedido é enviado para o ficheiro reservasPendestes.json e fica pendente de confirmação, quando o gestor ou funcionário confirmarem a reserva, o quarto fica ocupado nesse periodo de tempo.

-> Adição de um novo "método" para remover reservas que já foram confirmadas, só o funcionário e gestor podem remover essas reservas.
