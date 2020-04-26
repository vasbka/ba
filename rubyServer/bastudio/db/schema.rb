# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# This file is the source Rails uses to define your schema when running `rails
# db:schema:load`. When creating a new database, `rails db:schema:load` tends to
# be faster and is potentially less error prone than running all of your
# migrations from scratch. Old migrations may fail to apply correctly if those
# migrations use external dependencies or application code.
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 2020_04_20_014923) do

  create_table "clients", force: :cascade do |t|
    t.string "firstName"
    t.string "lastName"
    t.string "phoneNumber"
    t.datetime "created_at", precision: 6, null: false
    t.datetime "updated_at", precision: 6, null: false
    t.string "instagram"
  end

  create_table "exp_procedures", force: :cascade do |t|
    t.integer "price"
    t.integer "expert_id", null: false
    t.integer "procedure_id", null: false
    t.datetime "created_at", precision: 6, null: false
    t.datetime "updated_at", precision: 6, null: false
    t.integer "time_consume"
    t.index ["expert_id"], name: "index_exp_procedures_on_expert_id"
    t.index ["procedure_id"], name: "index_exp_procedures_on_procedure_id"
  end

  create_table "exp_procedures_time_slots", id: false, force: :cascade do |t|
    t.integer "time_slot_id"
    t.integer "exp_procedure_id"
    t.index ["exp_procedure_id"], name: "index_exp_procedures_time_slots_on_exp_procedure_id"
    t.index ["time_slot_id"], name: "index_exp_procedures_time_slots_on_time_slot_id"
  end

  create_table "experts", force: :cascade do |t|
    t.string "firstName"
    t.string "lastName"
    t.string "secondName"
    t.string "description"
    t.string "phoneNumber"
    t.datetime "created_at", precision: 6, null: false
    t.datetime "updated_at", precision: 6, null: false
  end

  create_table "procedure_categories", force: :cascade do |t|
    t.string "name"
    t.text "description"
  end

  create_table "procedure_categories_procedures", id: false, force: :cascade do |t|
    t.integer "procedure_id"
    t.integer "procedure_category_id"
    t.index ["procedure_category_id"], name: "index_procedure_categories_procedures_on_procedure_category_id"
    t.index ["procedure_id"], name: "index_procedure_categories_procedures_on_procedure_id"
  end

  create_table "procedures", force: :cascade do |t|
    t.string "title"
    t.text "description"
    t.datetime "created_at", precision: 6, null: false
    t.datetime "updated_at", precision: 6, null: false
  end

  create_table "time_slots", force: :cascade do |t|
    t.datetime "created_at", precision: 6, null: false
    t.datetime "updated_at", precision: 6, null: false
    t.integer "price"
    t.date "date"
    t.time "startTime"
    t.time "endTime"
    t.string "client_first_name"
    t.string "client_phone_number"
    t.string "additional"
    t.integer "client_id"
    t.index ["client_id"], name: "index_time_slots_on_client_id"
  end

  add_foreign_key "exp_procedures", "experts"
  add_foreign_key "exp_procedures", "procedures"
  add_foreign_key "time_slots", "clients"
end
