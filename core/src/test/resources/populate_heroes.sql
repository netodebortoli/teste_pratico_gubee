INSERT INTO power_stats(id, strength, agility, dexterity, intelligence)
VALUES ('96f75629-ea38-4e32-9fcb-0656db9ac9cc', 8, 8, 9, 10);
INSERT INTO power_stats(id, strength, agility, dexterity, intelligence)
VALUES ('b43b2b7c-1f4f-4685-b8b8-b22f4b1faa60', 9, 5, 7, 8);
INSERT INTO power_stats(id, strength, agility, dexterity, intelligence)
VALUES ('a67e6f28-fa28-49af-9869-631a856581d2', 10, 7, 8, 7);

INSERT INTO hero (id, name, race, power_stats_id)
VALUES ('c8a70a49-b174-4466-a35d-6f8a8a6fb243', 'BATMAN', 'HUMAN', '96f75629-ea38-4e32-9fcb-0656db9ac9cc');
INSERT INTO hero (id, name, race, power_stats_id)
VALUES ('1f117d30-a8a0-4735-a5f5-3759072bf499', 'BANE', 'HUMAN', 'b43b2b7c-1f4f-4685-b8b8-b22f4b1faa60');
INSERT INTO hero (id, name, race, power_stats_id)
VALUES ('52414f6d-333f-4dae-b50a-fb927819a368', 'AQUAMAN', 'HUMAN', 'a67e6f28-fa28-49af-9869-631a856581d2');
