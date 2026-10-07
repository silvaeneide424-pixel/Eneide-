# Security Specification — Vegan Flow Multi-Tenant RLS

## 1. Data Invariants
1. **Authentication Mandatory**: No read or write is permitted anywhere in the database unless `request.auth != null`.
2. **Strict Multi-Tenant Ownership (RLS)**: Every collection is nested under `/users/{userId}` and every document carries `userId == request.auth.uid`. A user can only read, list, create, update, or delete documents where both the path `{userId}` AND `resource.data.userId` (or `request.resource.data.userId`) equal `request.auth.uid`.
3. **Immutable Ownership & Creation Metadata**: On `update`, `userId`, `id`, and `createdAt` must remain strictly equal to `resource.data`.
4. **Server Timestamp Enforcement**: All `createdAt` and `updatedAt` fields must be valid Firestore `timestamp` values (`<= request.time`) populated via `FieldValue.serverTimestamp()`.
5. **Strict Schema & Key Allowlisting**: Every `create` and `update` operation validates exact allowed keys (`hasOnly`) and field types/sizes via standalone `isValid*` functions.

## 2. The "Dirty Dozen" Adversarial Payloads
1. **Unauthenticated Read**: `unauthDb.collection("users/alice/produtos").get()` -> Reject (`PERMISSION_DENIED`).
2. **Cross-Tenant Read**: `aliceDb.doc("users/bob/produtos/prod-1").get()` -> Reject (`PERMISSION_DENIED`).
3. **Cross-Tenant List**: `aliceDb.collection("users/bob/clientes").get()` -> Reject (`PERMISSION_DENIED`).
4. **Cross-Tenant Write**: `aliceDb.doc("users/bob/pedidos/ped-1").set(...)` -> Reject (`PERMISSION_DENIED`).
5. **Identity Spoofing on Create**: `aliceDb.doc("users/alice/produtos/prod-1").set({ userId: "bob", ... })` -> Reject (`PERMISSION_DENIED`).
6. **Ownership Mutation on Update**: `aliceDb.doc("users/alice/produtos/prod-1").update({ userId: "bob" })` -> Reject (`PERMISSION_DENIED`).
7. **Shadow / Ghost Field Injection**: `aliceDb.doc("users/alice/produtos/prod-1").update({ isAdmin: true })` -> Reject (`PERMISSION_DENIED`).
8. **CreatedAt Tampering on Update**: `aliceDb.doc("users/alice/clientes/cli-1").update({ createdAt: newTimestamp })` -> Reject (`PERMISSION_DENIED`).
9. **Oversized String DoS**: `aliceDb.doc("users/alice/produtos/prod-1").set({ nome: "A".repeat(5000) })` -> Reject (`PERMISSION_DENIED`).
10. **Invalid Numeric Type (String for Price)**: `aliceDb.doc("users/alice/produtos/prod-1").set({ preco: "free" })` -> Reject (`PERMISSION_DENIED`).
11. **Future Timestamp Spoofing**: `aliceDb.doc("users/alice/planos/pl-1").set({ createdAt: futureTimestamp })` -> Reject (`PERMISSION_DENIED`).
12. **Invalid Document ID Poisoning**: `aliceDb.doc("users/alice/produtos/bad$id!").set(...)` -> Reject (`PERMISSION_DENIED`).
