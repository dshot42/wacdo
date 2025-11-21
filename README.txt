test : Nom: Pierre Gagnaire

drag drop les employees vers les restaurants pouvoir dropper sur plusieurs restaurants de la list
ou simplement un menu deroulant pour choisir l'employer a ajouter au restau


probleme avec type number theamleaf et jpa attr int/float

refaire mes test !

/*****************/
Problèmes connus avec CriteriaBuilder et Hibernate 6.x

Depuis Hibernate 6, certaines constructions Criteria avec LEFT JOIN + ON + OR ont été modifiées / optimisées.

Symptômes classiques :

LEFT JOIN transformé en INNER JOIN si tu filtres dans WHERE
Predicates combinés avec cb.or(...) + subquery peuvent retourner 0 résultats même si SQL généré manuellement fonctionne
Hibernate “optimise” les joins si tu n’utilises pas explicitement une colonne de la table jointe.

/*****************/

todo test
dev projet avancer
pb map les assignements
deployement a regarder sous render ou https://www.koyeb.com/
linker a mon github