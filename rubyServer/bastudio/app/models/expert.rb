class Expert < ApplicationRecord
  has_many :exp_procedures
  has_many :procedures, through: :exp_procedures, dependent: :destroy
  has_many :time_slots, :through => :exp_procedures
  has_many :expert_days

  def as_json(options={})
   super(options.merge methods: [:exp_procedures])
  end

  def toString
    "#{lastName} #{firstName} #{secondName}"
  end


end
