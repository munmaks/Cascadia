
wildlife = ["BEAR", "ELK", "FOX", "SALMON", "HAWK"]

habitat = ["MOUNTAIN", "FOREST", "PRARIE", "WETLAND", "RIVER"]

new_pair = []

for area in habitat:
	for i in range(len(wildlife)-1):
		for j in range(i, len(wildlife)):
			if (wildlife[i] != wildlife[j]):
				new_pair.append(f"{area} {wildlife[i]} {wildlife[j]}")

with open("configSquareHabitatTile.txt", "w", encoding="utf-8") as f:
	for line in new_pair:
		f.write(line + "\n")
